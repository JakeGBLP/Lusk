package it.jakegblp.lusk.api.listeners;

import it.jakegblp.lusk.api.events.PlayerInventorySlotDropEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {
        PlayerInventorySlotDropEvent.setLastInventoryClickEvent(event);
    }
}
