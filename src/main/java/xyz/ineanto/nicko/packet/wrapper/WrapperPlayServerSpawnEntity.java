package xyz.ineanto.nicko.packet.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * This packet is sent by the server when a player comes into visible range, not when a player joins.
 */
public class WrapperPlayServerSpawnEntity extends AbstractPacket {
    /**
     * The packet type that is wrapped by this wrapper.
     */
    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY;

    /**
     * Constructors a new wrapper for the specified packet
     */
    public WrapperPlayServerSpawnEntity() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    /**
     * Sets the entity id of the player
     *
     * @param value New value for field 'entityId'
     */
    public void setEntityId(int value) {
        this.handle.getIntegers().writeSafely(0, value);
        this.handle.getEntityTypeModifier().writeSafely(0, EntityType.PLAYER);
    }

    /**
     * Sets the unique id of the player
     *
     * @param value New value for field 'playerId'
     */
    public void setPlayerId(UUID value) {
        this.handle.getUUIDs().writeSafely(0, value);
    }

    /**
     * Sets the value of field 'x'
     *
     * @param value New value for field 'x'
     */
    public void setX(double value) {
        this.handle.getDoubles().writeSafely(0, value);
    }

    /**
     * Sets the value of field 'y'
     *
     * @param value New value for field 'y'
     */
    public void setY(double value) {
        this.handle.getDoubles().writeSafely(1, value);
    }

    /**
     * Sets the value of field 'z'
     *
     * @param value New value for field 'z'
     */
    public void setZ(double value) {
        this.handle.getDoubles().write(2, value);
    }

    /**
     * Sets the discrete rotation around the y-axis (yaw)
     *
     * @param value New value for field 'yRot'
     */
    public void setYRotRaw(byte value) {
        this.handle.getBytes().writeSafely(0, value);
    }

    /**
     * Sets the discrete rotation around the x-axis (pitch)
     *
     * @param value New value for field 'xRot'
     */
    public void setXRotRaw(byte value) {
        this.handle.getBytes().writeSafely(1, value);
    }

    public void setLocation(@Nonnull Location location) {
        setX(location.getX());
        setY(location.getY());
        setZ(location.getZ());
        setYRotRaw(degreesToAngle(location.getYaw()));
        setXRotRaw(degreesToAngle(location.getPitch()));
    }

    private byte degreesToAngle(float degree) {
        return (byte)((int)(degree * 256.0F / 360.0F));
    }
}