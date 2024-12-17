package it.jakegblp.lusk.api.events;

import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AnvilGuiOpenEvent extends AnvilGuiEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    public AnvilGuiOpenEvent(AnvilGuiWrapper anvil, Player player) {
        super(anvil, player);
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
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
        if (cancel) {
            getAnvil().close();
        }
    }
}
