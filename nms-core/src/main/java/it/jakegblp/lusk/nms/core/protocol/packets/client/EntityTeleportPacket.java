package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
public class EntityTeleportPacket implements ClientboundPacketWithId {

    protected int id;
    protected Vector position;
    protected @NotNull Number yaw, pitch;
    @Availability(addedIn = "1.21.2")
    protected Vector velocity;
    protected boolean onGround;

    public EntityTeleportPacket(int id, Vector position, float yaw, float pitch, boolean onGround) {
        this(id, position, yaw, pitch, new Vector(), onGround);
    }

    @Availability(addedIn = "1.21.2")
    public EntityTeleportPacket(int id, Vector position, float yaw, float pitch, Vector velocity, boolean onGround) {
        this.id = id;
        this.position = position;
        if (NMS.getVersion().isGreaterOrEqual(Version.of(1,21,2))) {
            this.yaw = yaw;
            this.pitch = pitch;
        } else {
            this.yaw = (byte) (yaw / 360F * 256F);
            this.pitch = (byte) (pitch / 360F * 256F);
        }
        this.velocity = velocity;
        this.onGround = onGround;
    }

    public EntityTeleportPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Availability(addedIn = "1.21.2")
    public Vector getVelocity() {
        return velocity;
    }

    @Availability(addedIn = "1.21.2")
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(id);
        buffer.writeVector(position);
        if (NMS.getVersion().isGreaterOrEqual(Version.of(1,21,2))) {
            buffer.writeVector(velocity);
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
        id = buffer.readVarInt();
        position = buffer.readVector();
        if (NMS.getVersion().isGreaterOrEqual(Version.of(1,21,2))) {
            velocity = buffer.readVector();
            yaw = buffer.readFloat();
            pitch = buffer.readFloat();
        } else {
            yaw = buffer.readByte();
            pitch = buffer.readByte();
        }
        onGround = buffer.readBoolean();
    }

    @Override
    public EntityTeleportPacket copy() {
        return new EntityTeleportPacket(id, position.clone(), yaw.floatValue(), pitch.floatValue(), velocity.clone(), onGround);
    }
}
