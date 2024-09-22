package xyz.ineanto.nicko.wrapper;

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

    public void setUUIDs(List<UUID> value) {
        handle.getUUIDLists().writeSafely(0, value);
    }
}
