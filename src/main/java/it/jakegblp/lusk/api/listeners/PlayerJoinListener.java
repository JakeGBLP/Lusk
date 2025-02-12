package it.jakegblp.lusk.api.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static it.jakegblp.lusk.utils.NMSUtils.NMS;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        assert NMS != null;
        NMS.injectPlayer(event.getPlayer());
    }
}
