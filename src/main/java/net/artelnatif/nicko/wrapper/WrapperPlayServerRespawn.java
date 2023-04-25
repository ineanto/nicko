package net.artelnatif.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;

import java.util.Optional;

/**
 * 1.19.4 compliant version of the WrapperPlayServerRespawn.
 *
 * @author ineanto, based on work from dmulloy2 and Kristian S. Strangeland
 */
public class WrapperPlayServerRespawn extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.RESPAWN;

    public WrapperPlayServerRespawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public Optional<World> getDimension() {
        return handle.getDimensionTypes().optionRead(0);
    }

    public void setDimension(World value) {
        handle.getDimensionTypes().write(0, value);
    }

    public Optional<Long> getSeed() {
        return handle.getLongs().optionRead(0);
    }

    public void setSeed(long value) {
        handle.getLongs().write(0, value);
    }
}
