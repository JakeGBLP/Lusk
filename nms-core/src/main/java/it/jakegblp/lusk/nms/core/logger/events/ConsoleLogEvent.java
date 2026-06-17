package it.jakegblp.lusk.nms.core.logger.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ConsoleLogEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    protected boolean cancelled;
    protected String message;

    public ConsoleLogEvent(boolean async, String message){
        super(async);
        this.message = message;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
