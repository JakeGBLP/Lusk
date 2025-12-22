package it.jakegblp.lusk.nms.core.world.player;

import it.jakegblp.lusk.common.Instances;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GlowMap implements Listener {

    static {
        Instances.LUSK.getServer().getPluginManager().registerEvents(new GlowMap(), Instances.LUSK);
    }

    public static final ConcurrentHashMap<Player, Set<Integer>> glowMap = new ConcurrentHashMap<>(); // viewer -> glowing entity ids

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        glowMap.remove(player);

        int entityId = player.getEntityId();
        for (Set<Integer> set : glowMap.values()) {
            set.remove(entityId);
        }
    }

    public static void addGlow(Player viewer, int glowingEntityId) {
        glowMap.computeIfAbsent(viewer, k -> ConcurrentHashMap.newKeySet())
                .add(glowingEntityId);
    }

    public static void addGlow(Player viewer, Set<Integer> glowingEntityId) {
        glowMap.computeIfAbsent(viewer, k -> ConcurrentHashMap.newKeySet())
                .addAll(glowingEntityId);
    }

    public static void removeGlow(Player viewer, int glowingEntityId) {
        Set<Integer> set = glowMap.get(viewer);
        if (set != null) set.remove(glowingEntityId);
    }

    public static void removeGlow(Player viewer, Set<Integer> glowingEntityId) {
        Set<Integer> set = glowMap.get(viewer);
        if (set != null) set.removeAll(glowingEntityId);
    }


}
