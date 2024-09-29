package xyz.ineanto.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.common.hash.Hashing;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;

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

            try {
                final Object spawnInfoStructureHandle = spawnInfoStructure.getHandle();
                final RecordComponent[] components = spawnInfoStructureHandle.getClass().getRecordComponents();

                // Doesn't work!
                final Field levelKeyField = spawnInfoStructureHandle.getClass().getDeclaredField(components[1].getAccessor().getName());
                levelKeyField.setAccessible(true);
                levelKeyField.set(spawnInfoStructureHandle, BukkitConverters.getWorldKeyConverter().getGeneric(Bukkit.getWorld("world")));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException();
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
