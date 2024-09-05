package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import it.jakegblp.lusk.api.listeners.*;
import it.jakegblp.lusk.utils.PaperUtils;
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

    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");
        try {
            addon.loadClasses("it.jakegblp.lusk", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (PaperUtils.HAS_PLAYER_JUMP_EVENT && PaperUtils.HAS_ENTITY_JUMP_EVENT) {
            Lusk.registerListeners(new JumpListener.PaperJumpListener());
        }
        registerListeners(
                new UpdateChecker(this),
                new JumpListener.SpigotJumpListener(),
                new JumpListener.PaperJumpListener(),
                new JumpListener(),
                new RightClickListener(),
                new BlockBreakListener(),
                new HealListener(),
                new DeathListener(),
                new DamageListener(),
                new PlayerItemDropListener(),
                new InventoryClickListener()
        );
        instance.getLogger().info("Has been enabled!");
        int pluginId = 17730;
        Metrics metrics = new Metrics(this, pluginId);
        UpdateChecker.checkForUpdate(getDescription().getVersion());
    }

    public static Lusk getInstance() {
        return instance;
    }

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, instance);
        }
    }

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public SkriptAddon getAddonInstance() {
        return addon;
    }
}