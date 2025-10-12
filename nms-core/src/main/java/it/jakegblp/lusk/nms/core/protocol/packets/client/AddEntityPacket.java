package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import it.jakegblp.lusk.nms.core.annotations.Availability;

import java.util.UUID;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Spawn_Entity">Entity Spawn Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AddEntityPacket implements ClientboundPacketWithId {

    protected int entityId;
    protected UUID entityUUID;
    protected double x, y, z, headYaw;
    protected float pitch, yaw;
    protected EntityType entityType;
    protected int data;
    protected Vector velocity;
    @Availability(addedIn = "1.19")

    public AddEntityPacket(
            int id,
            UUID entityUUID,
            Location location,
            EntityType entityType,
            int data,
            Vector velocity) {
        this(id, entityUUID, location.x(), location.y(), location.z(), location.getYaw(), location.getPitch(), entityType, data, velocity, 0);
    }

    public AddEntityPacket(
            int id,
            UUID entityUUID,
            Vector vector,
            float pitch,
            float yaw,
            EntityType entityType,
            int data,
            Vector velocity) {
        this(id, entityUUID, vector.getX(), vector.getY(), vector.getZ(), pitch, yaw, entityType, data, velocity, 0);
    }

    public AddEntityPacket(
            int id,
            UUID entityUUID,
            double x,
            double y,
            double z,
            float pitch,
            float yaw,
            EntityType entityType,
            int data,
            Vector velocity) {
        this(id, entityUUID, x, y, z, pitch, yaw, entityType, data, velocity, 0);
    }

    public AddEntityPacket(
            int id,
            UUID entityUUID,
            double x,
            double y,
            double z,
            float pitch,
            float yaw,
            EntityType entityType,
            int data,
            Vector velocity,
            @Availability(addedIn = "1.19")
            double headYaw) {
        this.entityId = id;
        this.entityUUID = entityUUID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.entityType = entityType;
        this.data = data;
        this.velocity = velocity;
        this.headYaw = headYaw;
    }

    @Availability(addedIn = "1.19")
    public double getHeadYaw() {
        return headYaw;
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSAddEntityPacket(this);
    }
}
