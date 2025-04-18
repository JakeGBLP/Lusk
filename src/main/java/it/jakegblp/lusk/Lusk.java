package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.Version;
import it.jakegblp.lusk.api.listeners.*;
import it.jakegblp.lusk.utils.BorrowedUtils;
import it.jakegblp.lusk.utils.Constants;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static it.jakegblp.lusk.utils.LuskUtils.consoleLog;

/**
 * Small parts of this class are taken from SkBee
 */
@SuppressWarnings({"unused", "deprecation"})
public class Lusk extends JavaPlugin {
    @Getter
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
                new JumpListener.SpigotJumpListener(),
                new JumpListener(),
                new RightClickListener(),
                new BlockBreakListener(),
                new HealListener(),
                new DeathListener(),
                new ResurrectListener(),
                new DamageListener(),
                new PlayerItemDropListener(),
                new InventoryClickListener(),
                new AnvilGuiOpenListener(),
                new AnvilGuiClickListener(),
                new AnvilGuiCloseListener()
        );

        int pluginId = 17730;
        Metrics metrics = new Metrics(this, pluginId);
        String version = getDescription().getVersion().toLowerCase();
        String buildType = getBuildType();

        metrics.addCustomChart(new DrilldownPie("version_type", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();

            String localBuildType = getBuildType();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(localBuildType, 1);

            map.put(switch (localBuildType) {
                case "beta" -> "Beta";
                case "prerelease" -> "Prerelease";
                case "alpha" -> "Alpha";
                case "release" -> "Release";
                default -> "Other";
            }, entry);

            return map;
        }));

        metrics.addCustomChart(new DrilldownPie("skript_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();

            Version skriptVersion = Skript.getVersion();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(skriptVersion.toString(), 1);

            map.put(skriptVersion.getMajor()+"."+skriptVersion.getMinor()+"."+skriptVersion.getRevision(), entry);

            return map;
        }));

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
            consoleLog(" - {0} {1}{2}", finish[i], elementNames[i], finish[i] == 1 ? "" : "s");
        }
        if (!buildType.equals("release")) {
            consoleLog("&eThis is a {0} build and should not be used in production unless stated to be stable.", buildType);
            consoleLog("&eReport bugs at https://github.com/JakeGBLP/Lusk/issues");
        }
        new UpdateChecker(this);
        long end = System.currentTimeMillis();
        consoleLog("&aLusk {0} has been enabled! &8[&7{1}&8]", version, new Timespan(end - start));
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

    public String getBuildType() {
        String version = getDescription().getVersion().toLowerCase();
        if (version.contains("beta")) {
            return "beta";
        } else if (version.contains("pre")) {
            return "prerelease";
        } else if (version.contains("alpha")) {
            return "alpha";
        }
        return "release";
    }
}