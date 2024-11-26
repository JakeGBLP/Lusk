package it.jakegblp.lusk.api.events;

import it.jakegblp.lusk.api.wrappers.AnvilGuiWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class AnvilGuiEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final AnvilGuiWrapper anvil;

    public AnvilGuiEvent(AnvilGuiWrapper anvil, Player player) {
        super(player);
        this.anvil = anvil;
    }

    public Inventory getInventory() {
        return anvil.getAnvilGUI().getInventory();
    }

    public AnvilGuiWrapper getAnvil() {
        return anvil;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
