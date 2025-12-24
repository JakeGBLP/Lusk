package it.jakegblp.lusk.nms.core.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class SoundEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();


    protected Sound sound;
    protected SoundCategory soundSource;
    protected double x,y,z;
    protected float pitch,volume;
    protected long seed;

    protected boolean isEntitySound;
    protected int entityID;
    protected Entity entity;


    boolean isCancelled;

    public SoundEvent(@NotNull Player who, boolean async) {
        super(who, async);
    }

    public Location getLocation(){
        return new Location(player.getWorld(), x, y, z);
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
