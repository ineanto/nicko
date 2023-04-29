package net.artelnatif.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.hash.Hashing;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

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
    // - 1.19.2 reverted 1.18 and simply need a World ResourceKey.
    //
    // n.b.: this field is a nightmare please mojang stop refactoring
    // your code to change things that were working perfectly fine before
    //.............

    public World getDimension() {
        if (MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            return handle.getWorldKeys().read(0);
        }

        return handle.getDimensionTypes().read(0);
    }

    public void setDimension(World value) {
        if (MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            // 1.19 and above
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

    public GameMode getGameMode() {
        return handle.getGameModes().read(0).toBukkit();
    }

    public void setGameMode(GameMode value) {
        handle.getGameModes().write(0, EnumWrappers.NativeGameMode.fromBukkit(value));
    }

    //.............
    // Previous GameMode Field
    //.............

    public GameMode getPreviousGameMode() {
        return handle.getGameModes().read(1).toBukkit();
    }

    public void setPreviousGameMode(GameMode value) {
        handle.getGameModes().write(1, EnumWrappers.NativeGameMode.fromBukkit(value));
    }

    //.............
    // Copy Metadata Field
    //.............

    public boolean isCopyMetadata() {
        return handle.getBytes().read(0) != 0;
    }

    public void setCopyMetadata(boolean value) {
        handle.getBytes().write(0, ((byte) (value ? 0x01 : 0x00)));
    }

    //.............
    // Last death location Field
    // Added in 1.19.
    //.............

    public Optional<BlockPosition> getLastDeathLocation() {
        if (MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            return handle.getOptionals(BlockPosition.getConverter()).read(0);
        }
        return Optional.empty();
    }

    public void setLastDeathLocation(Location value) {
        if (MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            final BlockPosition locationToBlockPosition = BlockPosition.getConverter().getSpecific(value);
            final Optional<BlockPosition> blockPosition = Optional.ofNullable(locationToBlockPosition);
            handle.getOptionals(BlockPosition.getConverter()).write(0, blockPosition);
        }
    }

    //.............
    // Seed Field
    // Added in 1.15.
    //.............

    public long getSeed() {
        if (MinecraftVersion.BEE_UPDATE.atOrAbove()) {
            return handle.getLongs().read(0);
        }
        return -1;
    }

    public void setSeed(long value) {
        if (MinecraftVersion.BEE_UPDATE.atOrAbove()) {
            handle.getLongs().write(0, Hashing.sha256().hashLong(value).asLong());
        }
    }

    //.............
    // Difficulty Field
    // Removed in 1.14.
    //.............

    public Difficulty getDifficulty() {
        if (MinecraftVersion.VILLAGE_UPDATE.atOrAbove()) {
            return null;
        }

        final EnumWrappers.Difficulty difficulty = handle.getDifficulties().read(0);
        return difficulty == null ? null : Difficulty.valueOf(difficulty.name());
    }

    public void setDifficulty(Difficulty difficulty) {
        if (difficulty != null && !MinecraftVersion.VILLAGE_UPDATE.atOrAbove()) {
            handle.getDifficulties().write(0, EnumWrappers.Difficulty.valueOf(difficulty.name()));
        }
    }
}
