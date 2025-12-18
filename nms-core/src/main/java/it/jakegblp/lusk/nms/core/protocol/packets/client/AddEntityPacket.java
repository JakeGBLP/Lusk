package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@EqualsAndHashCode // todo: move headYaw field to earlier in the constructors
public class AddEntityPacket implements ClientboundPacketWithId {

    protected int id;
    protected @NotNull UUID entityUUID;
    protected double x, y, z, headYaw;
    protected float pitch, yaw;
    protected @NotNull EntityType entityType;
    protected int data;
    // might turn into 3 doubles
    protected @NotNull Vector velocity;

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            @NotNull Location location,
            @NotNull EntityType entityType,
            int data,
            @NotNull Vector velocity,
            double headYaw) {
        this(id, entityUUID, location.x(), location.y(), location.z(), location.getYaw(), location.getPitch(), entityType, data, velocity, headYaw);
    }

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            @NotNull Vector position,
            float pitch,
            float yaw,
            @NotNull EntityType entityType,
            int data,
            @NotNull Vector velocity,
            double headYaw) {
        this(id, entityUUID, position.getX(), position.getY(), position.getZ(), pitch, yaw, entityType, data, velocity, headYaw);
    }

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            double x,
            double y,
            double z,
            float pitch,
            float yaw,
            @NotNull EntityType entityType,
            int data,
            @NotNull Vector velocity,
            double headYaw) {
        this.id = id;
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

    @Override
    public Object asNMS() {
        return NMS.toNMSAddEntityPacket(this);
    }
}
