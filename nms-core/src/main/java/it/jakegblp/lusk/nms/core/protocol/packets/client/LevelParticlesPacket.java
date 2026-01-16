package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

@AllArgsConstructor
@Getter
@Setter
public class LevelParticlesPacket implements BufferSerializableClientboundPacket {

    protected double x;
    protected double y;
    protected double z;
    protected float xOffset;
    protected float yOffset;
    protected float zOffset;
    protected float maxSpeed;
    protected int count;
    protected boolean overrideLimiter;
    protected boolean alwaysShow;
    protected Particle particle;

    public LevelParticlesPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public Vector getPosition() {
        return new Vector(x, y, z);
    }

    public void setPosition(Vector position) {
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
    }

    public Vector getOffset() {
        return new Vector(xOffset, yOffset, zOffset);
    }

    public void setOffset(Vector offset) {
        xOffset = (float) offset.getX();
        yOffset = (float) offset.getY();
        zOffset = (float) offset.getZ();
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeBoolean(overrideLimiter);
        buffer.writeBoolean(alwaysShow);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeFloat(xOffset);
        buffer.writeFloat(yOffset);
        buffer.writeFloat(zOffset);
        buffer.writeFloat(maxSpeed);
        buffer.writeInt(count);
        buffer.writeParticle(particle);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        overrideLimiter = buffer.readBoolean();
        alwaysShow = buffer.readBoolean();
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        xOffset = buffer.readFloat();
        yOffset = buffer.readFloat();
        zOffset = buffer.readFloat();
        maxSpeed = buffer.readFloat();
        count = buffer.readInt();
        particle = buffer.readParticle();
    }

    @Override
    public LevelParticlesPacket copy() {
        return new LevelParticlesPacket(x, y, z, xOffset, yOffset, zOffset, maxSpeed, count, overrideLimiter, alwaysShow, particle);
    }
}
