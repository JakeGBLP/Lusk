package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.LevelParticlesPacket;
import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@Getter
@NullMarked
public class ParticleSendPacketEvent extends ClientPacketEvent<LevelParticlesPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected World world;
    protected Vector position, offset;
    protected int count;
    protected float maxSpeed;
    protected boolean overrideLimiter, alwaysShow;
    protected ParticleWrapper particle;

    public ParticleSendPacketEvent(LevelParticlesPacket packet, Player player, boolean async) {
        super(player, async);
        this.world = player.getWorld();
        this.position = packet.getPosition();
        this.particle = packet.getParticle();
        this.count = packet.getCount();
        this.offset = packet.getOffset();
        this.maxSpeed = packet.getMaxSpeed();
        this.overrideLimiter = packet.isOverrideLimiter();
        this.alwaysShow = packet.isAlwaysShow();
    }

    public Particle getBukkitParticle() {
        return particle.particle();
    }

    @Contract("-> new")
    public Location getLocation() {
        return position.toLocation(world);
    }

    @Contract("-> new")
    public Vector getPosition() {
        return position.clone();
    }

    @Contract("-> new")
    public Vector getOffset() {
        return offset.clone();
    }

    public void setPosition(Vector position) {
        this.position = position;
        markModified();
    }

    public void setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        markModified();
    }

    public void setCount(int count) {
        this.count = count;
        markModified();
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
        markModified();
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
        markModified();
    }

    public void setOverrideLimiter(boolean overrideLimiter) {
        this.overrideLimiter = overrideLimiter;
        markModified();
    }

    public void setParticle(ParticleWrapper particle) {
        this.particle = particle;
        markModified();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public LevelParticlesPacket createPacket() {
        return new LevelParticlesPacket(position, offset, maxSpeed, count, overrideLimiter, alwaysShow, particle);
    }
}
