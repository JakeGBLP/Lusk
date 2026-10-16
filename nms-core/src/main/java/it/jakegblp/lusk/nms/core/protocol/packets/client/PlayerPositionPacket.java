package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.PlayerPositionPacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithPosition;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithRotation;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithVelocity;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.entity.RelativeFlag;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NullMarked
public class PlayerPositionPacket implements ClientboundPacket<PlayerPositionPacketEvent>, PacketWithPosition, PacketWithVelocity, PacketWithRotation {

    protected int teleportId;
    protected double positionX, positionY, positionZ;
    protected float yaw, pitch;
    protected double velocityX, velocityY, velocityZ;
    protected Set<RelativeFlag> relativeFlags;

    public PlayerPositionPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public PlayerPositionPacket(
            int teleportId,
            double positionX,
            double positionY,
            double positionZ,
            float yaw,
            float pitch,
            double velocityX,
            double velocityY,
            double velocityZ,
            Set<RelativeFlag> relativeFlags
    ) {
        this.teleportId = teleportId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.relativeFlags = new HashSet<>(relativeFlags);
    }

    public PlayerPositionPacket(
            int teleportId,
            double positionX,
            double positionY,
            double positionZ,
            float yaw,
            float pitch,
            Set<RelativeFlag> relativeFlags
    ) {
        this.teleportId = teleportId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = new HashSet<>(relativeFlags);
    }

    public PlayerPositionPacket(
            int teleportId,
            Vector position,
            float yaw,
            float pitch,
            Vector velocity,
            Set<RelativeFlag> relativeFlags) {
        this(teleportId, position.getX(), position.getY(), position.getZ(), yaw, pitch, velocity.getX(), velocity.getY(), velocity.getZ(), relativeFlags);
    }

    public PlayerPositionPacket(int teleportId, Vector position, float yaw, float pitch, Set<RelativeFlag> relativeFlags) {
        this(teleportId, position, yaw, pitch, new Vector(), relativeFlags);
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
    public void setPosition(Vector position) {
        positionX = position.getX();
        positionY = position.getY();
        positionZ = position.getZ();
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(teleportId);
        buffer.writeVector(getPosition());
        buffer.writeVector(getVelocity());
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
        buffer.writeInt(RelativeFlag.pack(relativeFlags));
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        teleportId = buffer.readVarInt();
        setPosition(buffer.readVector());
        setVelocity(buffer.readVector());
        yaw = buffer.readFloat();
        pitch = buffer.readFloat();
        relativeFlags = RelativeFlag.unpack(buffer.readInt());
    }

    @Override
    public PlayerPositionPacketEvent createEvent(Player player, boolean async) {
        return new PlayerPositionPacketEvent(this, player, async);
    }

    @Override
    public PlayerPositionPacket copy() {
        return new PlayerPositionPacket(teleportId, positionX, positionY, positionZ, yaw, pitch, velocityX, velocityY, velocityZ, relativeFlags);
    }
}
