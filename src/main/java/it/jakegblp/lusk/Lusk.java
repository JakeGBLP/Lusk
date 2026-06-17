package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import it.jakegblp.lusk.common.Instances;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.logger.ConsoleLogInjector;
import it.jakegblp.lusk.nms.core.logger.events.ConsoleLogEvent;
import it.jakegblp.lusk.nms.factory.NMSFactory;
import it.jakegblp.lusk.skript.factory.SkriptAdapterFactory;
import it.jakegblp.lusk.skript.modules.base.BaseModule;
import it.jakegblp.lusk.skript.modules.packets.PacketsModule;
import it.jakegblp.lusk.skript.modules.playerprofile.PlayerProfileModule;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import lombok.Getter;
import org.apache.commons.text.WordUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.SkriptAddon;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
public final class Lusk extends JavaPlugin {
    public static Lusk instance;
    private Version serverVersion, skriptVersion;
    private String rawVersion;
    private boolean hookedWithSkript;

    public static boolean isPluginEnabled(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public void onEnable() {
        instance = this;
        Instances.LUSK = this;
        serverVersion = Version.parse(Bukkit.getBukkitVersion().split("[^\\d.]")[0]);
        NMS = NMSFactory.createNMS(this, serverVersion);

        hookedWithSkript = isPluginEnabled("Skript");
        if (hookedWithSkript) {
            skriptVersion = Version.parse(Skript.getVersion().toString().split("[^\\d.]")[0]);
            AddonUtils.sectionContextAdapter = SkriptAdapterFactory.createSkriptAdapter(skriptVersion);
            SkriptAddon addon = Skript.instance().registerAddon(getClass(), "Lusk");
            addon.localizer().setSourceDirectories("lang", null);
            addon.loadModules(new BaseModule(), new PacketsModule(), new PlayerProfileModule());
            int[] elementCountBefore = getSkriptElementCount();
            int[] elementCountAfter = getSkriptElementCount();
            int[] finish = new int[elementCountBefore.length];
            int total = 0;
            for (int i = 0; i < elementCountBefore.length; i++) {
                finish[i] = elementCountAfter[i] - elementCountBefore[i];
                total += finish[i];
            }
            String[] elementNames = new String[]{"event", "effect", "expression", "condition", "type", "section", "structure"};

            getLogger().info("Loaded "+total+" elements:");
            for (int i = 0; i < finish.length; i++) {
                getLogger().info(" - "+finish[i]+" "+elementNames[i]+(finish[i] == 1 ? "" : "s"));
            }
            Instances.SKRIPT_ADDON = addon;
            getLogger().info("Hooked with Skript!");
        }
        int pluginId = 17730;
        Metrics metrics = new Metrics(this, pluginId);
        rawVersion = getPluginMeta().getVersion();
        String buildType = getBuildType();

        ConsoleLogInjector.inject(l -> {
            boolean async = !getServer().isPrimaryThread();

            ConsoleLogEvent event = new ConsoleLogEvent(async, l.messageStrippedAnsi());

            getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                l.cancel();
            }
        });

        metrics.addCustomChart(new DrilldownPie("version_type", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(buildType, 1);
            map.put(WordUtils.capitalizeFully(buildType), entry);
            return map;
        }));

        metrics.addCustomChart(new DrilldownPie("skript_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(skriptVersion.toString(), 1);
            map.put(skriptVersion.major()+"."+skriptVersion.minor()+"."+skriptVersion.patch(), entry);
            return map;
        }));
        if (!buildType.equals("release")) {
            getLogger().info("This is a "+buildType+" build and should not be used in production unless stated to be stable.");
            getLogger().info("Report bugs at https://github.com/JakeGBLP/Lusk/issues");
        }
        getLogger().info("Lusk has been enabled!");
        getLogger().info("- Server version: " + serverVersion);
        if (hookedWithSkript)
            getLogger().info("- Skript version: " + skriptVersion);
    }

    @Override
    public void onDisable() {
        if (NMS != null) {
            NMS = null;
        }
        getLogger().info("Lusk has been disabled!");
        ConsoleLogInjector.uninject();
    }

    public String getBuildType() {
        String version = rawVersion.toLowerCase();
        if (version.contains("beta")) {
            return "beta";
        } else if (version.contains("pre")) {
            return "prerelease";
        } else if (version.contains("alpha")) {
            return "alpha";
        }
        return "release";
    }

    public int[] getSkriptElementCount() {
        int[] i = new int[7];
        i[0] = Skript.getEvents().size();
        i[1] = Skript.getEffects().size();
        AtomicInteger exprs = new AtomicInteger();
        Skript.getExpressions().forEachRemaining(e -> exprs.getAndIncrement());
        i[2] = exprs.get();
        i[3] = Skript.getConditions().size();
        i[4] = 0;//Classes.getClassInfos().size();
        i[5] = Skript.getSections().size();
        i[6] = Skript.getStructures().size();
        return i;
    }
}
