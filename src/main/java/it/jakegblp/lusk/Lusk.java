package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings("unused")
public class Lusk extends JavaPlugin {
    private static Lusk instance;
    private SkriptAddon addon;

    @SuppressWarnings("unused")
    public static Lusk getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("it.jakegblp.lusk", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pluginId = 17730;
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, pluginId);
        instance.getLogger().info("Has been enabled!");
        new UpdateChecker(this, 108428).getVersion(version -> {
            String thisVersion = this.getDescription().getVersion();
            if (thisVersion.equals(version)) {
                instance.getLogger().info("There is not a new update available.");
            } else {
                instance.getLogger().info("There is a new update available. " + "[" + thisVersion + " -> " + version + "]");
            }
        });
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener,this);
    }
    @SuppressWarnings("unused")
    public SkriptAddon getAddonInstance() {
        return addon;
    }
}