package it.jakegblp.lusk.api.listeners;

import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.api.events.PlayerInventorySlotDropEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerItemDropListener implements Listener {

    @EventHandler
    public static void onDrop(PlayerDropItemEvent event) {
        Lusk.callEvent(new PlayerInventorySlotDropEvent(PlayerInventorySlotDropEvent.getLastInventoryClickEvent(), event));
    }
}
