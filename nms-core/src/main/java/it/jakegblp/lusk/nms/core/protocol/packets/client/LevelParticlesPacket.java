package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.ParticleSendPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;

@AllArgsConstructor
@Getter
@Setter
public class LevelParticlesPacket implements BufferSerializableClientboundPacket<ParticleSendPacketEvent> {

    protected double x, y, z;
    protected float xOffset, yOffset, zOffset, maxSpeed;
    protected int count;
    protected boolean overrideLimiter, alwaysShow;
    protected ParticleWrapper particle;

    public LevelParticlesPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public LevelParticlesPacket(Vector position, Vector offset, float maxSpeed, int count, boolean overrideLimiter, boolean alwaysShow, ParticleWrapper particle) {
        setPosition(position);
        setOffset(offset);
        this.maxSpeed = maxSpeed;
        this.count = count;
        this.overrideLimiter = overrideLimiter;
        this.alwaysShow = alwaysShow;
        this.particle = particle;
    }

    @Contract("-> new")
    public Vector getPosition() {
        return new Vector(x, y, z);
    }

    public void setPosition(Vector position) {
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
    }

    @Contract("-> new")
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
    public ParticleSendPacketEvent createEvent(Player player, boolean async) {
        return new ParticleSendPacketEvent(this, player, async);
    }

    @Override
    public LevelParticlesPacket copy() {
        return new LevelParticlesPacket(x, y, z, xOffset, yOffset, zOffset, maxSpeed, count, overrideLimiter, alwaysShow, particle);
    }
}
