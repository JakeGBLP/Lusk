package it.jakegblp.lusk.nms.core.injection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class InjectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        NMS.injectPlayer(event.getPlayer(), NMS.getPlugin());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        NMS.uninjectPlayer(event.getPlayer());
    }
}
