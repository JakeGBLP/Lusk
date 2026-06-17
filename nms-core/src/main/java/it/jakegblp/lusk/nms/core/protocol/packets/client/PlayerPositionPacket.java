package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.event.client.PlayerPositionPacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericPositionPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericRotationPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericVelocityPacket;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.RelativeFlag;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
@NullMarked
public class PlayerPositionPacket implements BufferSerializableClientboundPacket<PlayerPositionPacketEvent>, GenericPositionPacket, GenericVelocityPacket, GenericRotationPacket {

    protected int teleportId;
    protected double positionX, positionY, positionZ;
    protected float yaw, pitch;
    @Availability(addedIn = "1.21.2")
    protected double velocityX, velocityY, velocityZ;
    protected Set<RelativeFlag> relativeFlags;

    public PlayerPositionPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Availability(addedIn = "1.21.2")
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

    @Availability(addedIn = "1.21.2")
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

    public static boolean hasVelocity() {
        return NMS.getVersion().isGreaterOrEqual(Version.of(1, 21, 2));
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
    public void setPosition(Vector position) {
        positionX = position.getX();
        positionY = position.getY();
        positionZ = position.getZ();
    }

    @Availability(addedIn = "1.21.2")
    public double getVelocityX() {
        if (!hasVelocity()) throw new RuntimeException("Velocity in he player position packet is not available until 1.21.2");
        return velocityX;
    }
    @Availability(addedIn = "1.21.2")
    public double getVelocityY() {
        if (!hasVelocity()) throw new RuntimeException("Velocity in he player position packet is not available until 1.21.2");
        return velocityY;
    }
    @Availability(addedIn = "1.21.2")
    public double getVelocityZ() {
        if (!hasVelocity()) throw new RuntimeException("Velocity in he player position packet is not available until 1.21.2");
        return velocityZ;
    }

    @Availability(addedIn = "1.21.2")
    public void setVelocityX(double x) {
        if (!hasVelocity()) throw new RuntimeException("Velocity in he player position packet is not available until 1.21.2");
        velocityX = x;
    }
    @Availability(addedIn = "1.21.2")
    public void setVelocityY(double y) {
        if (!hasVelocity()) throw new RuntimeException("Velocity in he player position packet is not available until 1.21.2");
        velocityY = y;
    }
    @Availability(addedIn = "1.21.2")
    public void setVelocityZ(double z) {
        if (!hasVelocity()) throw new RuntimeException("Velocity in he player position packet is not available until 1.21.2");
        velocityZ = z;
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        if (hasVelocity()) {
            buffer.writeVarInt(teleportId);
            buffer.writeVector(getPosition());
            buffer.writeVector(getVelocity());
            buffer.writeFloat(yaw);
            buffer.writeFloat(pitch);
            buffer.writeInt(RelativeFlag.pack(relativeFlags));
        } else {
            buffer.writeVector(getPosition());
            buffer.writeFloat(yaw);
            buffer.writeFloat(pitch);
            buffer.writeByte(RelativeFlag.pack(relativeFlags));
            buffer.writeVarInt(teleportId);
        }
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        if (hasVelocity()) {
            teleportId = buffer.readVarInt();
            setPosition(buffer.readVector());
            setVelocity(buffer.readVector());
            yaw = buffer.readFloat();
            pitch = buffer.readFloat();
            relativeFlags = RelativeFlag.unpack(buffer.readInt());
        } else {
            setPosition(buffer.readVector());
            yaw = buffer.readFloat();
            pitch = buffer.readFloat();
            relativeFlags = RelativeFlag.unpack(buffer.readUnsignedByte());
            teleportId = buffer.readVarInt();
        }
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
