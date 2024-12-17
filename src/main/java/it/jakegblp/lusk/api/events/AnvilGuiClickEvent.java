package it.jakegblp.lusk.api.events;

import it.jakegblp.lusk.api.AnvilGuiWrapper;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AnvilGuiClickEvent extends AnvilGuiSnapshotEvent {
    private static final HandlerList handlers = new HandlerList();
    private final int slot;

    public AnvilGuiClickEvent(AnvilGuiWrapper anvil, AnvilGUI.StateSnapshot snapshot, int slot) {
        super(anvil, snapshot);
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
