package xyz.ineanto.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.*;
import com.google.common.hash.Hashing;
import org.bukkit.GameMode;
import org.bukkit.World;

/**
 * PacketPlayServerRespawn Wrapper class (1.19 to 1.21 (one day))
 *
 * @author ineanto, based on work from dmulloy2 and Kristian S. Strangeland
 * <p>
 * In 1.20.2, all the fields were replaced with a
 * single "CommonPlayerSpawnInfo" record object.
 */
public class WrapperPlayServerRespawn extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.RESPAWN;

    private final InternalStructure commonPlayerSpawnInfoStructure;

    public WrapperPlayServerRespawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
        commonPlayerSpawnInfoStructure = handle.getStructures().readSafely(0);
    }

    public void setDimension(World value) {
        if (commonPlayerSpawnInfoStructure == null) {
            System.out.println("cPSIS null");
            // 1.19 to 1.20.1, props to lukalt for helping me figure this out.
            writeDimensionToStructure(value, handle.getStructures(), handle.getWorldKeys());
            return;
        }

        // 1.20.2 to 1.21
        writeDimensionToStructure(value, commonPlayerSpawnInfoStructure.getStructures(), commonPlayerSpawnInfoStructure.getWorldKeys());
    }

    public void setGameMode(GameMode value) {
        if (MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            commonPlayerSpawnInfoStructure.getGameModes().writeSafely(0, EnumWrappers.NativeGameMode.fromBukkit(value));
            return;
        }
        handle.getGameModes().writeSafely(0, EnumWrappers.NativeGameMode.fromBukkit(value));
    }

    public void setPreviousGameMode(GameMode value) {
        if (MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            commonPlayerSpawnInfoStructure.getGameModes().writeSafely(1, EnumWrappers.NativeGameMode.fromBukkit(value));
            return;
        }
        handle.getGameModes().writeSafely(1, EnumWrappers.NativeGameMode.fromBukkit(value));
    }

    public void setCopyMetadata(boolean value) {
        if (MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) return;
        if (MinecraftVersion.FEATURE_PREVIEW_UPDATE.atOrAbove()) {
            handle.getBytes().writeSafely(0, ((byte) (value ? 0x01 : 0x00)));
        } else {
            handle.getBooleans().writeSafely(0, value);
        }
    }

    public void setSeed(long value) {
        if (MinecraftVersion.WILD_UPDATE.atOrAbove() && !MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            handle.getLongs().writeSafely(0, Hashing.sha256().hashLong(value).asLong());
        }
    }

    private void writeDimensionToStructure(World value, StructureModifier<InternalStructure> structures, StructureModifier<World> worldKeys) {
        if (MinecraftVersion.TRAILS_AND_TAILS.atOrAbove() && !MinecraftVersion.v1_20_5.atOrAbove()) {
            final InternalStructure dimensionType = structures.readSafely(0);
            if (dimensionType != null) {
                // 1.20.2 to 1.20.5
                dimensionType.getMinecraftKeys().writeSafely(0, new MinecraftKey("minecraft", "dimension_type"));
                dimensionType.getMinecraftKeys().writeSafely(1, new MinecraftKey("minecraft", "overworld"));
                structures.writeSafely(0, dimensionType);
            }
        } else {
            // 1.20.5/6 to 1.21
            final StructureModifier<World> worldHolder = commonPlayerSpawnInfoStructure.getHolders(
                    MinecraftReflection.getDimensionManager(),
                    Converters.holder(BukkitConverters.getDimensionConverter(),
                            WrappedRegistry.getDimensionRegistry())
            );
            worldHolder.writeSafely(0, value);
        }

        worldKeys.writeSafely(0, value);
    }
}
