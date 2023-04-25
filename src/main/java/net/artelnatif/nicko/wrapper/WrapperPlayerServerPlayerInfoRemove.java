package net.artelnatif.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import java.util.List;
import java.util.UUID;

/**
 * Up-to-date version of the Wrapper class
 * for the PlayerServerPlayerInfoRemove.
 *
 * @author ineanto, based on work from dmulloy2 and Kristian S. Strangeland
 */

public class WrapperPlayerServerPlayerInfoRemove extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.PLAYER_INFO_REMOVE;

    public WrapperPlayerServerPlayerInfoRemove() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayerServerPlayerInfoRemove(PacketContainer packet) {
        super(packet, TYPE);
    }

    public List<UUID> getUUIDs() {
        return handle.getUUIDLists().read(0);
    }

    public void setUUIDs(List<UUID> value) {
        handle.getUUIDLists().write(0, value);
    }
}
