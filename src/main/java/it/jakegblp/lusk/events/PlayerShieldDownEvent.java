package it.jakegblp.lusk.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerShieldDownEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ItemStack shield;

    public PlayerShieldDownEvent(@NotNull Player player, ItemStack shield) {
        super(player);
        this.shield = shield;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public ItemStack getShield() {
        return shield;
    }
}
