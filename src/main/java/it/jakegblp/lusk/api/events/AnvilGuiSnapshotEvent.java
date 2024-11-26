package it.jakegblp.lusk.api.events;

import it.jakegblp.lusk.api.wrappers.AnvilGuiWrapper;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AnvilGuiSnapshotEvent extends AnvilGuiEvent {
    private static final HandlerList handlers = new HandlerList();

    private final AnvilGUI.StateSnapshot snapshot;

    public AnvilGuiSnapshotEvent(AnvilGuiWrapper anvil, AnvilGUI.StateSnapshot snapshot) {
        super(anvil, snapshot.getPlayer());
        this.snapshot = snapshot;
    }

    // todo: add snapshot getter
    public AnvilGUI.StateSnapshot getSnapshot() {
        return snapshot;
    }

    public String getText() {
        return snapshot.getText();
    }

    public ItemStack getLeftItem() {
        return snapshot.getLeftItem();
    }

    public ItemStack getRightItem() {
        return snapshot.getRightItem();
    }

    public ItemStack getOutputItem() {
        return snapshot.getOutputItem();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
