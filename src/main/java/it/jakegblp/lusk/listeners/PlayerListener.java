package it.jakegblp.lusk.listeners;

import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.events.PlayerEntityCollideEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

public class PlayerListener implements Listener {
    private final Lusk plugin;

    public PlayerListener(Lusk plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                } else {
                    BoundingBox hitbox = player.getBoundingBox();
                    for (Entity entity : player.getWorld().getNearbyEntities(hitbox)) {
                        if (entity != player && entity.getBoundingBox().overlaps(hitbox))
                            Bukkit.getPluginManager().callEvent(new PlayerEntityCollideEvent(player, entity));
                    }
                }
            }
        }.runTaskTimer(this.plugin, 0, 1);
    }
}
