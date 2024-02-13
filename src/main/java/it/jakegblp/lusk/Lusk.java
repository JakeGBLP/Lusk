package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.vdurmont.semver4j.Semver;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static it.jakegblp.lusk.utils.Utils.Version;

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
        Metrics metrics = new Metrics(this, pluginId);
        instance.getLogger().info("Has been enabled!");
        new UpdateChecker(this, 108428).getVersion(v -> {
            Semver local = Version(this.getDescription().getVersion()), published = Version(v);
            if (published.isGreaterThan(local)) {
                instance.getLogger().info("A new Lusk update is available. [" + local + " -> " + published + "]");
            } else if (local.isGreaterThan(published)) {
                instance.getLogger().info("You're running an unreleased version of Lusk. (Latest is "+published+" but you're running "+local+")");
            } else {
                instance.getLogger().info("Lusk is up to date!");
            }
        });
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener,instance);
    }
    @SuppressWarnings("unused")
    public SkriptAddon getAddonInstance() {
        return addon;
    }
}