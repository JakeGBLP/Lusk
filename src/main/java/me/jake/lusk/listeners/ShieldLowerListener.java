package me.jake.lusk.listeners;

import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import me.jake.lusk.events.PlayerShieldDownEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ShieldLowerListener implements Listener {
    @EventHandler
    public void onStopUsingItem(PlayerStopUsingItemEvent event) {
        ItemStack item = event.getItem();
        if (item.getType() == Material.SHIELD) {
            Bukkit.getPluginManager().callEvent(new PlayerShieldDownEvent(event.getPlayer(), item));
        }
    }
}
