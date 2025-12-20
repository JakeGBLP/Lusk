package it.jakegblp.lusk.nms.core.util;

import it.jakegblp.lusk.common.Instances;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class Displayable {

    private final Set<UUID> viewers = new HashSet<>();
    private int taskID;

    protected Displayable(boolean persistent) {
        taskID = persistent ? -1 : -2;
    }

    protected abstract void displayImplementation(Player player);
    protected abstract void removeImplementation(Player player);

    public boolean isPersistent() {
        return taskID != -2;
    }

    public boolean isDisplayed(Player player) {
        return viewers.contains(player.getUniqueId());
    }

    public void addViewer(Player player, JavaPlugin plugin) {
        if (viewers.add(player.getUniqueId())) {
            displayImplementation(player);
            if (isPersistent())
                startLoopIfNeeded(plugin);
        }
    }

    public void addViewers(Collection<? extends Player> players, JavaPlugin plugin) {
        for (Player player : players)
            addViewer(player, plugin);
    }

    public void addViewers(Collection<? extends Player> players) {
        addViewers(players, Instances.LUSK);
    }

    public void addViewers(Player[] players, JavaPlugin plugin) {
        for (Player player : players)
            addViewer(player, plugin);
    }

    public void addViewers(Player[] players) {
        addViewers(players, Instances.LUSK);
    }

    public void removeViewer(Player player) {
        if (viewers.remove(player.getUniqueId())) {
            removeImplementation(player);
            if (isPersistent())
                stopLoopIfEmpty();
        }
    }

    public void removeViewers(Collection<? extends Player> players) {
        for (Player player : players)
            removeViewer(player);
    }

    public void removeViewers(Player[] players) {
        for (Player player : players)
            removeViewer(player);
    }

    private void startLoopIfNeeded(JavaPlugin plugin) {
        if (taskID != -1) return;
        taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::loopIteration, 20L, 20L).getTaskId();
    }

    private void startLoopIfNeeded() {
        startLoopIfNeeded(Instances.LUSK);
    }

    private void stopLoopIfEmpty() {
        if (viewers.isEmpty() && taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
            taskID = -1;
        }
    }

    private void loopIteration() {
        if (viewers.isEmpty()) {
            stopLoopIfEmpty();
            return;
        }

        for (Iterator<UUID> iterator = viewers.iterator(); iterator.hasNext(); ) {
            UUID uuid = iterator.next();
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                iterator.remove();
                continue;
            }
            displayImplementation(player);
        }
    }

    public Set<UUID> getViewers() {
        return Collections.unmodifiableSet(viewers);
    }
}
