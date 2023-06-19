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
 */
public class WrapperPlayServerRespawn extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.RESPAWN;

    public WrapperPlayServerRespawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    //.............
    // Dimension/World Field
    // The dimension field has changed numerous times:
    // - 1.8 through 1.17 (?) need an integer,
    // - 1.18 need a Holder of a World ResourceKey,
    // - 1.19 and beyond don't require a Holder.
    //
    // n.b.: this field is a nightmare please mojang stop refactoring
    // your code to change things that were working perfectly fine before
    //.............

    public void setDimension(World value) {
        if (MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            // 1.19 to 1.19.4
            // Thank you lukalt!
            final InternalStructure dimensionType = handle.getStructures().read(0);
            dimensionType.getMinecraftKeys().write(0, new MinecraftKey("minecraft", "dimension_type"));
            dimensionType.getMinecraftKeys().write(1, new MinecraftKey("minecraft", "overworld"));
            handle.getStructures().write(0, dimensionType);
            handle.getWorldKeys().write(0, value);
        } else if (MinecraftVersion.CAVES_CLIFFS_2.atOrAbove()) {
            // 1.18
            handle.getHolders(
                    MinecraftReflection.getDimensionManager(),
                    BukkitConverters.getDimensionConverter()
            ).write(0, value);
        } else {
            // 1.17 and below (untested)
            handle.getDimensions().write(0, value.getEnvironment().ordinal());
        }
    }

    //.............
    // GameMode Field
    //.............

    public void setGameMode(GameMode value) {
        handle.getGameModes().write(0, EnumWrappers.NativeGameMode.fromBukkit(value));
    }

    //.............
    // Previous GameMode Field
    //.............

    public void setPreviousGameMode(GameMode value) {
        handle.getGameModes().write(1, EnumWrappers.NativeGameMode.fromBukkit(value));
    }

    //.............
    // Copy Metadata Field
    //.............

    public void setCopyMetadata(boolean value) {
        if(MinecraftVersion.FEATURE_PREVIEW_UPDATE.atOrAbove()) {
            handle.getBytes().write(0, ((byte) (value ? 0x01 : 0x00)));
        } else {
            handle.getBooleans().write(0, value);
        }
    }

    //.............
    // Seed Field
    // Added in 1.15.
    //.............

    public void setSeed(long value) {
        if (MinecraftVersion.BEE_UPDATE.atOrAbove()) {
            handle.getLongs().write(0, Hashing.sha256().hashLong(value).asLong());
        }
    }

    //.............
    // Difficulty Field
    // Removed in 1.14.
    //.............

    public void setDifficulty(Difficulty difficulty) {
        if (difficulty != null && !MinecraftVersion.VILLAGE_UPDATE.atOrAbove()) {
            handle.getDifficulties().write(0, EnumWrappers.Difficulty.valueOf(difficulty.name()));
        }
    }
}
