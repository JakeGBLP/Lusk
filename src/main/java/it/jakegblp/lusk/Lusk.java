package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.listeners.*;
import it.jakegblp.lusk.utils.BorrowedUtils;
import it.jakegblp.lusk.utils.Constants;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static it.jakegblp.lusk.utils.LuskUtils.consoleLog;

/**
 * Small parts of this class are taken from SkBee
 */
@SuppressWarnings("unused")
public class Lusk extends JavaPlugin {
    private static Lusk instance;
    private SkriptAddon addon;

    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");
        int[] elementCountBefore = BorrowedUtils.getElementCount();

        try {
            addon.loadClasses("it.jakegblp.lusk", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Constants.PAPER_HAS_PLAYER_JUMP_EVENT && Constants.PAPER_HAS_ENTITY_JUMP_EVENT) {
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

        int pluginId = 17730;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("skript_version",() -> Skript.getVersion().toString()));
        String version = getDescription().getVersion().toLowerCase();
        String versionType = version.contains("beta") ? "beta" : (version.contains("pre") ? "prerelease" : (version.contains("alpha") ? "alpha" : "release"));
        metrics.addCustomChart(new SimplePie("lusk_version_type",() -> versionType));

        int[] elementCountAfter = BorrowedUtils.getElementCount();
        int[] finish = new int[elementCountBefore.length];
        int total = 0;
        for (int i = 0; i < elementCountBefore.length; i++) {
            finish[i] = elementCountAfter[i] - elementCountBefore[i];
            total += finish[i];
        }
        String[] elementNames = new String[]{"event", "effect", "expression", "condition", "section"};

        consoleLog("Loaded {0} elements:", total);
        for (int i = 0; i < finish.length; i++) {
            consoleLog(" {0} {1}{2}", finish[i], elementNames[i], finish[i] == 1 ? "" : "s");
        }
        if (!versionType.equals("release")) {
            consoleLog("<yellow>This is a {0} build, which may not be stable unless stated otherwise on the release page. Proceed with caution.", versionType);
            consoleLog("<yellow>https://github.com/JakeGBLP/Lusk/issues");
        }
        new UpdateChecker(this);
        long end = System.currentTimeMillis();
        consoleLog("<green>Lusk {0} has been enabled! <dark_gray>[<gray>{1}</gray>]", version, new Timespan(end - start));
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