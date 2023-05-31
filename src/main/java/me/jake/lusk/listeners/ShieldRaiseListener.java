package me.jake.lusk.listeners;

import me.jake.lusk.events.PlayerShieldUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShieldRaiseListener implements Listener {
    public ShieldRaiseListener() {
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (event.getAction().isRightClick() && item != null && item.getType() == Material.SHIELD) {
            Bukkit.getPluginManager().callEvent(new PlayerShieldUpEvent(event.getPlayer(), item));
        }
    }
}
