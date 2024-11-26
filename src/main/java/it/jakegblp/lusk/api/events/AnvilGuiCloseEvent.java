package it.jakegblp.lusk.api.events;

import it.jakegblp.lusk.api.wrappers.AnvilGuiWrapper;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AnvilGuiCloseEvent extends AnvilGuiSnapshotEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    public AnvilGuiCloseEvent(AnvilGuiWrapper anvil, AnvilGUI.StateSnapshot snapshot) {
        super(anvil, snapshot);
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
        if (cancel) {
            getAnvil().open(player);
            cancelled = true;
        }
    }
}
