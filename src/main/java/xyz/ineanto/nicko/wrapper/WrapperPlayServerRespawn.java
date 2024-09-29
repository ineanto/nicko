package xyz.ineanto.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.common.hash.Hashing;
import org.bukkit.GameMode;
import org.bukkit.World;

import java.lang.reflect.Field;

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

    private InternalStructure spawnInfoStructure = null;

    public WrapperPlayServerRespawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
        if (MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            spawnInfoStructure = handle.getStructures().read(0);
        }
    }

    public void setDimension(World value) {
        final MinecraftVersion v1_20_5 = new MinecraftVersion(1, 20, 5);

        if (!MinecraftVersion.getCurrentVersion().isAtLeast(v1_20_5)) {
            // 1.20 - 1.20.4
            final StructureModifier<InternalStructure> structureModifier = spawnInfoStructure == null ?
                    handle.getStructures() : spawnInfoStructure.getStructures();

            final StructureModifier<World> worldStructureModifier = spawnInfoStructure == null ?
                    handle.getWorldKeys() : spawnInfoStructure.getWorldKeys();

            final InternalStructure dimensionType = structureModifier.read(0);
            dimensionType.getMinecraftKeys().writeSafely(0, new MinecraftKey("minecraft", "dimension_type"));
            dimensionType.getMinecraftKeys().writeSafely(1, new MinecraftKey("minecraft", "overworld"));
            structureModifier.writeSafely(0, dimensionType);
            worldStructureModifier.writeSafely(0, value);
        } else {
            // 1.20.5 to 1.21.1

            final Class<?> commonPlayerInfoClazz = MinecraftReflection.getMinecraftClass("network.protocol.game.CommonPlayerSpawnInfo");
            try {
                final Field commonSpawnDataField = Accessors.getFieldAccessor(TYPE.getPacketClass(), commonPlayerInfoClazz, true).getField();
                commonSpawnDataField.setAccessible(true);

                final MinecraftKey key = MinecraftKey.fromHandle(
                        Accessors.getFieldAccessor(
                                        commonPlayerInfoClazz,
                                        MinecraftReflection.getResourceKey(),
                                        true
                                )
                                .get(spawnInfoStructure));

                Accessors.getFieldAccessor(
                                commonPlayerInfoClazz,
                                MinecraftReflection.getResourceKey(),
                                true
                        )
                        .set(commonSpawnDataField.get(this), key);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
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
