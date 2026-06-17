package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.event.client.AddEntityPacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericPositionPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericRotationPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericVelocityPacket;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Spawn_Entity">Wiki</a>
 * {@link AddEntityPacketEvent AddEntityEvent}
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NullMarked
public class AddEntityPacket implements ClientboundPacketWithEntityId<AddEntityPacketEvent>, GenericPositionPacket, GenericRotationPacket, GenericVelocityPacket {

    protected int entityId;
    protected UUID entityUUID;
    protected double positionX, positionY, positionZ;
    protected float pitch, yaw, headYaw;
    protected double velocityX, velocityY, velocityZ;
    protected EntityType entityType;
    protected int data;

    public AddEntityPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public AddEntityPacket(
            int id,
            UUID entityUUID,
            Location location,
            float headYaw,
            Vector velocity,
            EntityType entityType,
            int data) {
        this(id, entityUUID, location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), headYaw, velocity.getX(), velocity.getY(), velocity.getX(), entityType, data);
    }

    public AddEntityPacket(
            int id,
            UUID entityUUID,
            Vector position,
            float pitch,
            float yaw,
            float headYaw,
            Vector velocity,
            EntityType entityType,
            int data) {
        this(id, entityUUID, position.getX(), position.getY(), position.getZ(), pitch, yaw, headYaw, velocity.getX(), velocity.getY(), velocity.getX(), entityType, data);
    }

    public AddEntityPacket(
            int id,
            UUID entityUUID,
            Vector position,
            float headYaw,
            Vector velocity,
            EntityType entityType,
            int data) {
        this(id, entityUUID, position.getX(), position.getY(), position.getZ(), 0, 0, headYaw, velocity.getX(), velocity.getY(), velocity.getX(), entityType, data);
    }

    /**
     * @return a location where the world is always null
     */
    @Contract("-> new")
    public Location getLocation() {
        return new Location(null, positionX, positionY, positionZ, yaw, pitch);
    }

    @Contract("-> new")
    public Vector getPosition() {
        return new Vector(positionX, positionY, positionZ);
    }

    public void setLocation(Location location) {
        this.positionX = location.getX();
        this.positionY = location.getY();
        this.positionZ = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public void setPosition(Vector position) {
        this.positionX = position.getX();
        this.positionY = position.getY();
        this.positionZ = position.getZ();
    }

    @Contract("-> new")
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
        buffer.writeVarInt(entityId);
        buffer.writeUUID(entityUUID);
        buffer.writeEntityType(entityType);
        buffer.writeDouble(positionX);
        buffer.writeDouble(positionY);
        buffer.writeDouble(positionZ);
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
        entityId = buffer.readVarInt();
        entityUUID = buffer.readUUID();
        entityType = buffer.readEntityType();
        positionX = buffer.readDouble();
        positionY = buffer.readDouble();
        positionZ = buffer.readDouble();
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
    public AddEntityPacketEvent createEvent(Player player, boolean async) {
        return new AddEntityPacketEvent(this, player, async);
    }

    @Override
    public AddEntityPacket copy() {
        return new AddEntityPacket(entityId, entityUUID, positionX, positionY, positionZ, pitch, yaw, headYaw, velocityX, velocityY, velocityZ, entityType, data);
    }
}
