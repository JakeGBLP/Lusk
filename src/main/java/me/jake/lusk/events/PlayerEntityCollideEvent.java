package me.jake.lusk.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerEntityCollideEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity entity;

    public PlayerEntityCollideEvent(@NotNull Player player, Entity entity) {
        super(player);
        this.entity = entity;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Entity getEntity() {
        return entity;
    }
}
