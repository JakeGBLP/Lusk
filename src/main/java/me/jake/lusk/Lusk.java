package me.jake.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.jake.lusk.listeners.ComponentClickRun;
import me.jake.lusk.listeners.PlayerListener;
import me.jake.lusk.listeners.ShieldLowerListener;
import me.jake.lusk.listeners.ShieldRaiseListener;
import me.jake.lusk.other.ComponentFilter;
import me.jake.lusk.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings("unused")
public class Lusk extends JavaPlugin {
    private static Lusk instance;
    public ComponentClickRun componentClickRun;
    private SkriptAddon addon;

    @SuppressWarnings("unused")
    public static Lusk getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);
        try {
            Utils.setEverything();
            addon.loadClasses("me.jake.lusk", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pluginId = 17730;
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, pluginId);
        instance.getLogger().info("Has been enabled!");
        PluginManager pluginManager = getServer().getPluginManager();
        if (Bukkit.getServer().getPluginManager().getPlugin("SkBee") != null) {
            ((Logger) LogManager.getRootLogger()).addFilter(new ComponentFilter());
            componentClickRun = new ComponentClickRun();
            pluginManager.registerEvents(componentClickRun, this);
        }
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new ShieldRaiseListener(), this);
        if (Skript.classExists("io.papermc.paper.event.player.PlayerStopUsingItemEvent")) {
            pluginManager.registerEvents(new ShieldLowerListener(), this);
        }
        new UpdateChecker(this, 108428).getVersion(version -> {
            String thisVersion = this.getDescription().getVersion();
            if (thisVersion.equals(version)) {
                instance.getLogger().info("There is not a new update available.");
            } else {
                instance.getLogger().info("There is a new update available. " + "[" + thisVersion + " -> " + version + "]");
            }
        });
    }

    @SuppressWarnings("unused")
    public SkriptAddon getAddonInstance() {
        return addon;
    }
}