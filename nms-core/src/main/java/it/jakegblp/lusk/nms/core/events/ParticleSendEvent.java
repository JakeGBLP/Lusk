package it.jakegblp.lusk.nms.core.events;

import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ParticleSendEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    protected ParticleWrapper particle;
    protected int count;
    protected World world;
    protected double x;
    protected double y;
    protected double z;
    protected float xOffset;
    protected float yOffset;
    protected float zOffset;
    protected float maxSpeed;

    protected boolean cancelled;


    public Particle getBukkitParticle(){
        return particle.particle();
    }


    public ParticleSendEvent(@NotNull Player who, boolean async) {
        super(who, async);
    }

    @Contract(pure = true)
    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    @Contract(pure = true)
    public Vector getOffset() {
        return new Vector(xOffset, yOffset, zOffset);
    }

}
