package it.jakegblp.lusk.nms.core.logger.events;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class LogEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    boolean cancelled;
    String message;


    public LogEvent (boolean async, String message){
        super(async);
        this.message = message;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
