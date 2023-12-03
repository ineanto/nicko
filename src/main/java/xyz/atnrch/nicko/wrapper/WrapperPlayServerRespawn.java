package xyz.atnrch.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.common.hash.Hashing;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;

/**
 * Up-to-date version of the Wrapper class
 * for the PacketPlayServerRespawn.
 *
 * @author ineanto, based on work from dmulloy2 and Kristian S. Strangeland
 * <p>
 * <p>
 * Packet changes history (not accurate)
 * <p>
 * In 1.20.2, all the fields were replaced with a
 * single "CommonPlayerSpawnInfo" record object.
 * <p>
 * The dimension field was changed numerous times:
 * - 1.8 through 1.17 (?) required an integer,
 * - 1.18 need an instance of a Holder of a ResourceKey,
 * - 1.19 and after dropped this requirement.
 * N.b.: this field is a nightmare please mojang stop refactoring
 * your code to change things that were working perfectly fine before
 * <p>
 * The Seed field was added in 1.15.
 * The Difficulty field was removed in 1.14.
 */
public class WrapperPlayServerRespawn extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.RESPAWN;

    private InternalStructure commonPlayerSpawnInfoStructure;

    public WrapperPlayServerRespawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
        if (MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            commonPlayerSpawnInfoStructure = handle.getStructures().read(0);
        }
    }

    public void setDimension(World value) {
        if (MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            // 1.20.2
            final InternalStructure dimensionType = commonPlayerSpawnInfoStructure.getStructures().read(0);
            dimensionType.getMinecraftKeys().writeSafely(0, new MinecraftKey("minecraft", "dimension_type"));
            dimensionType.getMinecraftKeys().writeSafely(1, new MinecraftKey("minecraft", "overworld"));
            commonPlayerSpawnInfoStructure.getStructures().writeSafely(0, dimensionType);
            commonPlayerSpawnInfoStructure.getWorldKeys().writeSafely(0, value);
        } else if (MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            // 1.19 to 1.20.1
            // Thank you lukalt!
            final InternalStructure dimensionType = handle.getStructures().read(0);
            dimensionType.getMinecraftKeys().writeSafely(0, new MinecraftKey("minecraft", "dimension_type"));
            dimensionType.getMinecraftKeys().writeSafely(1, new MinecraftKey("minecraft", "overworld"));
            handle.getStructures().writeSafely(0, dimensionType);
            handle.getWorldKeys().writeSafely(0, value);
        } else if (MinecraftVersion.CAVES_CLIFFS_2.atOrAbove()) {
            // 1.18
            handle.getHolders(
                    MinecraftReflection.getDimensionManager(),
                    BukkitConverters.getDimensionConverter()
            ).writeSafely(0, value);
        } else {
            // 1.17 and below (untested)
            handle.getDimensions().writeSafely(0, value.getEnvironment().ordinal());
        }
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
        if (MinecraftVersion.BEE_UPDATE.atOrAbove() && !MinecraftVersion.CONFIG_PHASE_PROTOCOL_UPDATE.atOrAbove()) {
            handle.getLongs().writeSafely(0, Hashing.sha256().hashLong(value).asLong());
        }
    }

    public void setDifficulty(Difficulty difficulty) {
        if (difficulty != null && !MinecraftVersion.VILLAGE_UPDATE.atOrAbove()) {
            handle.getDifficulties().writeSafely(0, EnumWrappers.Difficulty.valueOf(difficulty.name()));
        }
    }
}
