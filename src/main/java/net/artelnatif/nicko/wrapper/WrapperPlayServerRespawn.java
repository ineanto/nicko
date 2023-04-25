package net.artelnatif.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.EnumWrappers;
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
    // Dimension Field (1.8 - Present)
    // The dimension field has changed types,
    // numerous times. Version 1.8 through 1.15 need an integer,
    // 1.15 through 1.18 need a (NBT Tag) Identifier and
    // 1.19.2 and beyond require a Holder of a DimensionManager Identifier (???).
    // (Wiki.vg still refers this as an Identifier)
    //
    // n.b.: this field is a nightmare please mojang stop refactoring
    // your code to change things that were working perfectly fine before
    //.............

    public World getDimension() {
        if (MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            // 1.19 and above
            return handle.getHolders(
                    MinecraftReflection.getDimensionManager(),
                    BukkitConverters.getDimensionConverter()
            ).read(0);
        }

        return handle.getDimensionTypes().read(0);
    }

    public void setDimension(World value) {
        if (MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            // 1.19 and above
            handle.getWorldKeys().withParamType(
                    MinecraftReflection.getResourceKey(),
                    BukkitConverters.getWorldKeyConverter()
            ).write(0, value);
            return;
        }
        // 1.18 and below
        handle.getDimensionTypes().write(0, value);
    }

    //.............
    // GameMode Field
    //.............

    public void getGameMode() {
        // Present since 1.8, we're good!
        handle.getGameModes().read(0);
    }

    public void setGameMode(GameMode value) {
        // Present since 1.8, we're good!
        handle.getGameModes().write(0, EnumWrappers.NativeGameMode.fromBukkit(value));
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
            handle.getLongs().write(0, value);
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
