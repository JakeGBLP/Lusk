package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.event.client.EntityTeleportPacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericPositionPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericRotationPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericVelocityPacket;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
@NullMarked
@SuppressWarnings("NotNullFieldNotInitialized")
public class EntityTeleportPacket implements ClientboundPacketWithEntityId<EntityTeleportPacketEvent>, GenericPositionPacket, GenericVelocityPacket, GenericRotationPacket {

    protected int entityId;
    protected double positionX, positionY, positionZ;
    protected Number yaw, pitch;
    @Availability(addedIn = "1.21.2")
    protected double velocityX, velocityY, velocityZ;
    protected boolean onGround;

    public EntityTeleportPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Availability(addedIn = "1.21.2")
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

    public EntityTeleportPacket(
            int entityId,
            double positionX,
            double positionY,
            double positionZ,
            float yaw,
            float pitch,
            boolean onGround
    ) {
        this.entityId = entityId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Availability(addedIn = "1.21.2")
    public EntityTeleportPacket(int entityId, Vector position, float yaw, float pitch, Vector velocity, boolean onGround) {
        this.entityId = entityId;
        this.positionX = position.getX();
        this.positionY = position.getY();
        this.positionZ = position.getZ();
        if (hasVelocity()) {
            this.yaw = yaw;
            this.pitch = pitch;
        } else {
            this.yaw = (byte) (yaw / 360F * 256F);
            this.pitch = (byte) (pitch / 360F * 256F);
        }
        this.velocityX = velocity.getX();
        this.velocityY = velocity.getY();
        this.velocityZ = velocity.getZ();
        this.onGround = onGround;
    }
    public EntityTeleportPacket(
            int entityId,
            Vector position,
            float yaw,
            float pitch,
            boolean onGround
    ) {
        this(entityId, position, yaw, pitch, new Vector(), onGround);
    }

    public static boolean hasVelocity() {
        return NMS.getVersion().isGreaterOrEqual(Version.of(1, 21, 2));
    }

    @Override
    public float getPitch() {
        return pitch.floatValue();
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public float getYaw() {
        return yaw.floatValue();
    }

    @Availability(addedIn = "1.21.2")
    @Contract("-> new")
    public Vector getVelocity() {
        return new Vector(velocityX, velocityY, velocityZ);
    }

    @Availability(addedIn = "1.21.2")
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
        if (hasVelocity()) {
            buffer.writeVector(getVelocity());
            buffer.writeFloat(yaw.floatValue());
            buffer.writeFloat(pitch.floatValue());
        } else {
            buffer.writeByte(yaw.byteValue());
            buffer.writeByte(pitch.byteValue());
        }
        buffer.writeBoolean(onGround);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        entityId = buffer.readVarInt();
        setPosition(buffer.readVector());
        if (hasVelocity()) {
            setVelocity(buffer.readVector());
            yaw = buffer.readFloat();
            pitch = buffer.readFloat();
        } else {
            yaw = buffer.readByte();
            pitch = buffer.readByte();
        }
        onGround = buffer.readBoolean();
    }

    @Override
    public EntityTeleportPacketEvent createEvent(Player player, boolean async) {
        return new EntityTeleportPacketEvent(this, player, async);
    }

    @Override
    public EntityTeleportPacket copy() {
        return new EntityTeleportPacket(entityId, getPosition(), yaw.floatValue(), pitch.floatValue(), getVelocity(), onGround);
    }
}
