package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings("unused")
public class Lusk extends JavaPlugin {
    private static Lusk instance;
    private SkriptAddon addon;

    public static Lusk getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");
        try {
            addon.loadClasses("it.jakegblp.lusk", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pluginId = 17730;
        Metrics metrics = new Metrics(this, pluginId);
        instance.getLogger().info("Has been enabled!");
        UpdateChecker.checkForUpdate(getDescription().getVersion());
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }
    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }
    public SkriptAddon getAddonInstance() {
        return addon;
    }
}