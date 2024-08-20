package xyz.ineanto.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.common.hash.Hashing;
import org.bukkit.GameMode;
import org.bukkit.World;

/**
 * PacketPlayServerRespawn Wrapper class (1.20.X to 1.21.X)
 * <p>
 * In 1.20.2, all the fields were merged inside a
 * single "CommonPlayerSpawnInfo" record.
 *
 * @author inenato (w/ additional help from lukalt), based on work from dmulloy2 and Kristian S. Strangeland
 */
public class WrapperPlayServerRespawn extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.RESPAWN;

    private final InternalStructure spawnInfoStructure;

    public WrapperPlayServerRespawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
        spawnInfoStructure = handle.getStructures().readSafely(0);
    }

    public void setDimension(World value) {
        if (!MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            // 1.20 to 1.20.1 (by lukalt)
            final InternalStructure dimensionType = handle.getStructures().read(0);
            dimensionType.getMinecraftKeys().writeSafely(0, new MinecraftKey("minecraft", "dimension_type"));
            dimensionType.getMinecraftKeys().writeSafely(1, new MinecraftKey("minecraft", "overworld"));
            handle.getStructures().writeSafely(0, dimensionType);
            handle.getWorldKeys().writeSafely(0, value);
            return;
        }

        // 1.20.2 to 1.21
        if (MinecraftVersion.TRAILS_AND_TAILS.atOrAbove() && !MinecraftVersion.v1_21_0.atOrAbove()) {
            spawnInfoStructure.getHolders(
                    MinecraftReflection.getDimensionManager(),
                    BukkitConverters.getDimensionConverter()
            ).write(0, value);
            spawnInfoStructure.getWorldKeys().writeSafely(0, value);
            handle.getStructures().writeSafely(0, spawnInfoStructure);
        }
    }

    public void setGameMode(GameMode value) {
        if (!MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            // 1.20 to 1.20.1
            handle.getGameModes().writeSafely(0, EnumWrappers.NativeGameMode.fromBukkit(value));
            return;
        }

        spawnInfoStructure.getGameModes().writeSafely(0, EnumWrappers.NativeGameMode.fromBukkit(value));
    }

    public void setPreviousGameMode(GameMode value) {
        if (!MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            // 1.20 to 1.20.1
            handle.getGameModes().writeSafely(1, EnumWrappers.NativeGameMode.fromBukkit(value));
            return;
        }

        spawnInfoStructure.getGameModes().writeSafely(1, EnumWrappers.NativeGameMode.fromBukkit(value));
    }

    public void setCopyMetadata(boolean value) {
        if (!MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) return;

        // 1.20 to 1.20.1
        handle.getBooleans().writeSafely(0, value);
    }

    public void setSeed(long value) {
        if (!MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            // 1.20 to 1.20.1
            handle.getLongs().writeSafely(0, Hashing.sha256().hashLong(value).asLong());
        }
    }
}
