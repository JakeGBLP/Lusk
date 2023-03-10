package me.jake.lusk;

import java.io.IOException;

import me.jake.lusk.utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

public class Lusk extends JavaPlugin {

    private static Lusk instance;
    private SkriptAddon addon;


    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("me.jake.lusk", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pluginId = 17730;
        Metrics metrics = new Metrics(this, pluginId);
        Utils.setEverything();
        Bukkit.getLogger().info("[Lusk] has been enabled!");
    }

    public static Lusk getInstance() {
        return instance;
    }

    public SkriptAddon getAddonInstance() {
        return addon;
    }
}