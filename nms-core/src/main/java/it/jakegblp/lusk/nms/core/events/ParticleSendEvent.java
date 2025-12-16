package it.jakegblp.lusk.nms.core.events;

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
import org.jetbrains.annotations.NotNull;


@Getter
@Setter

public class ParticleSendEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();


    Particle particle;
    int count;
    World world;
    double x;
    double y;
    double z;
    float xOffset;
    float yOffset;
    float zOffset;
    float maxSpeed;

    boolean isCancelled;

    public ParticleSendEvent(@NotNull Player who, boolean async) {
        super(who, async);
    }


    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    public Vector getOffset() {
        return new Vector(xOffset, yOffset, zOffset);
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}
