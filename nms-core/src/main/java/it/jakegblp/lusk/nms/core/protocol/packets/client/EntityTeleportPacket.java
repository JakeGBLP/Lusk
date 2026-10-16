package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.EntityTeleportPacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithEntityId;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithPosition;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithRotation;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithVelocity;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@NullMarked
public class EntityTeleportPacket implements ClientboundPacket<EntityTeleportPacketEvent>,
        PacketWithPosition, PacketWithVelocity, PacketWithRotation, PacketWithEntityId {

    protected int entityId;
    protected double positionX, positionY, positionZ;
    protected float yaw, pitch;
    protected double velocityX, velocityY, velocityZ;
    protected boolean onGround;

    public EntityTeleportPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public EntityTeleportPacket(
            int entityId,
            double positionX,
            double positionY,
            double positionZ,
            float yaw,
            float pitch,
            double velocityX,
            double velocityY,
            double velocityZ,
            boolean onGround
    ) {
        this.entityId = entityId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.onGround = onGround;
    }

    public EntityTeleportPacket(int entityId, Vector position, float yaw, float pitch, Vector velocity, boolean onGround) {
        this(entityId, position.getX(), position.getY(), position.getZ(), yaw, pitch, velocity.getX(), velocity.getY(), velocity.getZ(), onGround);
    }

    public EntityTeleportPacket(int entityId, Location location, Vector velocity, boolean onGround) {
        this(entityId, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), velocity.getX(), velocity.getY(), velocity.getZ(), onGround);
    }

    @Contract("-> new")
    public Vector getVelocity() {
        return new Vector(velocityX, velocityY, velocityZ);
    }

    public void setVelocity(Vector velocity) {
        velocityX = velocity.getX();
        velocityY = velocity.getY();
        velocityZ = velocity.getZ();
    }

    @Override
    @Contract("-> new")
    public Vector getPosition() {
        return new Vector(positionX, positionY, positionZ);
    }

    @Override
    public void setPosition(Vector vector) {
        positionX = vector.getX();
        positionY = vector.getY();
        positionZ = vector.getZ();
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeVector(getVelocity());
        buffer.writeVector(getVelocity());
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
        buffer.writeBoolean(onGround);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        entityId = buffer.readVarInt();
        setPosition(buffer.readVector());
        setVelocity(buffer.readVector());
        yaw = buffer.readFloat();
        pitch = buffer.readFloat();
        onGround = buffer.readBoolean();
    }

    @Override
    public EntityTeleportPacketEvent createEvent(Player player, boolean async) {
        return new EntityTeleportPacketEvent(this, player, async);
    }

    @Override
    public EntityTeleportPacket copy() {
        return new EntityTeleportPacket(entityId, getPosition(), yaw, pitch, getVelocity(), onGround);
    }
}
