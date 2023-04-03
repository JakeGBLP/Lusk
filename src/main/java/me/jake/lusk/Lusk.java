package me.jake.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.jake.lusk.utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings("unused")
public class Lusk extends JavaPlugin {

    private static Lusk instance;
    private SkriptAddon addon;

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
        Bukkit.getLogger().info("[Lusk] has been enabled!");
    }

    @SuppressWarnings("unused")
    public static Lusk getInstance() {
        return instance;
    }

    @SuppressWarnings("unused")
    public SkriptAddon getAddonInstance() {
        return addon;
    }
}