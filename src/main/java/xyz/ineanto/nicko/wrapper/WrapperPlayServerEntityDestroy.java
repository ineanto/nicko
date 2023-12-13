package xyz.ineanto.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.Converters;
import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Sent by the server to the client to remove one or more entities.
 */
public class WrapperPlayServerEntityDestroy extends AbstractPacket {

    /**
     * The packet type that is wrapped by this wrapper.
     */
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_DESTROY;

    public WrapperPlayServerEntityDestroy() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    /**
     * Sets the list of entity ids to remove
     *
     * @param value New value for field 'entityIds'
     */
    public void setEntityIds(IntList value) {
        this.handle.getModifier().withType(IntList.class, Converters.passthrough(IntList.class)).writeSafely(0, value);
    }

}