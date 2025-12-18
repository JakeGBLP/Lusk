package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Spawn_Entity">Entity Spawn Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class AddEntityPacket implements ClientboundPacketWithId {

    protected int id;
    protected @NotNull UUID entityUUID;
    protected double x, y, z;
    protected float pitch, yaw;
    protected double headYaw, xVelocity, yVelocity, zVelocity;
    protected @NotNull EntityType entityType;
    protected int data;

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            @NotNull Location location,
            double headYaw,
            @NotNull Vector velocity,
            @NotNull EntityType entityType,
            int data) {
        this(id, entityUUID, location.x(), location.y(), location.z(), location.getYaw(), location.getPitch(), headYaw, velocity.getX(), velocity.getY(), velocity.getZ(), entityType, data);
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSAddEntityPacket(this);
    }

    public Vector getVelocity() {
        return new Vector(xVelocity, yVelocity, zVelocity);
    }

    /**
     * @return a location where the world is always null
     */
    public Location getPosition() {
        return new Location(null, x, y, z, yaw, pitch);
    }

    @Override
    protected AddEntityPacket clone() throws CloneNotSupportedException {
        return (AddEntityPacket) super.clone();
    }
}
