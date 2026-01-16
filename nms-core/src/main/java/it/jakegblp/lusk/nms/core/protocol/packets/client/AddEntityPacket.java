package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
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
    protected float pitch, yaw, headYaw;
    protected double velocityX, velocityY, velocityZ;
    protected @NotNull EntityType entityType;
    protected int data;

    public AddEntityPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            @NotNull Location location,
            float headYaw,
            @NotNull Vector velocity,
            @NotNull EntityType entityType,
            int data) {
        this(id, entityUUID, location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), headYaw, velocity.getX(), velocity.getY(), velocity.getX(), entityType, data);
    }

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            @NotNull Vector position,
            float pitch,
            float yaw,
            float headYaw,
            @NotNull Vector velocity,
            @NotNull EntityType entityType,
            int data) {
        this(id, entityUUID, position.getX(), position.getY(), position.getZ(), pitch, yaw, headYaw, velocity.getX(), velocity.getY(), velocity.getX(), entityType, data);
    }

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            @NotNull Vector position,
            float headYaw,
            @NotNull Vector velocity,
            @NotNull EntityType entityType,
            int data) {
        this(id, entityUUID, position.getX(), position.getY(), position.getZ(), 0, 0, headYaw, velocity.getX(), velocity.getY(), velocity.getX(), entityType, data);
    }

    /**
     * @return a location where the world is always null
     */
    public Location getPosition() {
        return new Location(null, x, y, z, yaw, pitch);
    }

    public void setPosition(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public void setPosition(Vector position) {
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
    }

    public Vector getVelocity() {
        return new Vector(velocityX, velocityY, velocityZ);
    }

    public void setVelocity(Vector velocity) {
        this.velocityX = velocity.getX();
        this.velocityY = velocity.getY();
        this.velocityZ = velocity.getZ();
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        boolean is1_21_9 = NMS.getVersion().isGreaterOrEqual(Version.of(1,21,9));
        buffer.writeVarInt(id);
        buffer.writeUUID(entityUUID);
        buffer.writeEntityType(entityType);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        if (is1_21_9)
            buffer.writeLowPrecisionVector(velocityX, velocityY, velocityZ);
        buffer.writePackedDegrees(pitch);
        buffer.writePackedDegrees(yaw);
        buffer.writePackedDegrees(headYaw);
        buffer.writeVarInt(data);
        if (!is1_21_9) {
            buffer.writeShort((short) velocityX);
            buffer.writeShort((short) velocityY);
            buffer.writeShort((short) velocityZ);
        }
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        boolean is1_21_9 = NMS.getVersion().isGreaterOrEqual(Version.of(1,21,9));
        id = buffer.readVarInt();
        entityUUID = buffer.readUUID();
        entityType = buffer.readEntityType();
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        if (is1_21_9) {
            var velocity = buffer.readLowPrecisionVector();
            velocityX = velocity[0];
            velocityY = velocity[1];
            velocityZ = velocity[2];
        }
        pitch = buffer.readPackedDegrees();
        yaw = buffer.readPackedDegrees();
        headYaw = buffer.readPackedDegrees();
        data = buffer.readVarInt();
        if (!is1_21_9) {
            velocityX = buffer.readShort();
            velocityY = buffer.readShort();
            velocityZ = buffer.readShort();
        }
    }

    @Override
    public AddEntityPacket copy() {
        return new AddEntityPacket(id, entityUUID, getPosition(), headYaw, getVelocity(), entityType, data);
    }
}
