package it.jakegblp.lusk.api.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class GenericEntityJumpEvent extends EntityEvent {


    public GenericEntityJumpEvent(@NotNull Entity what) {
        super(what);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
