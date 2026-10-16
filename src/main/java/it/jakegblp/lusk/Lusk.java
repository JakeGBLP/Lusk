package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import it.jakegblp.lusk.common.Instances;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.logger.ConsoleLogInjector;
import it.jakegblp.lusk.nms.core.logger.events.ConsoleLogEvent;
import it.jakegblp.lusk.nms.factory.NMSFactory;
import it.jakegblp.lusk.skript.modules.base.BaseModule;
import it.jakegblp.lusk.skript.modules.boundingbox.BoundingBoxModule;
import it.jakegblp.lusk.skript.modules.citizens.CitizensModule;
import it.jakegblp.lusk.skript.modules.console.ConsoleModule;
import it.jakegblp.lusk.skript.modules.packets.PacketsModule;
import it.jakegblp.lusk.skript.modules.playerprofile.PlayerProfileModule;
import lombok.Getter;
import org.apache.commons.lang3.text.WordUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

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
    @SuppressWarnings("deprecation")
    public void onEnable() {
        instance = this;
        Instances.LUSK = this;
        serverVersion = Version.parse(Bukkit.getBukkitVersion().split("[^\\d.]")[0]);
        NMS = NMSFactory.createNMS(this, serverVersion);
        NMS.createMetadataRegistry();
        hookedWithSkript = isPluginEnabled("Skript");
        if (hookedWithSkript) {
            skriptVersion = Version.parse(Skript.getVersion().toString().split("[^\\d.]")[0]);
            SkriptAddon addon = Skript.instance().registerAddon(getClass(), "Lusk");
            Instances.SKRIPT_ADDON = addon;
            addon.localizer().setSourceDirectories("lang", null);
            AtomicInteger atomicClassInfoAmount = new AtomicInteger();
            IntConsumer adder = atomicClassInfoAmount::addAndGet;
            addon.loadModules(
                    new BaseModule(adder),
                    new BoundingBoxModule(adder),
                    new PacketsModule(adder),
                    new PlayerProfileModule(adder),
                    new CitizensModule(adder),
                    new ConsoleModule(adder)
            );
            SyntaxRegistry.Key<? extends SyntaxInfo<?>>[] syntaxRegistryKeys = new SyntaxRegistry.Key<?>[] {
                    BukkitSyntaxInfos.Event.KEY, SyntaxRegistry.EXPRESSION, SyntaxRegistry.CONDITION, SyntaxRegistry.EFFECT, SyntaxRegistry.SECTION//, SyntaxRegistry.STRUCTURE
            };
            var syntaxRegistry = addon.syntaxRegistry();
            int classInfoAmount = atomicClassInfoAmount.get();
            getLogger().info("Loaded " + (syntaxRegistry.elements().stream().filter(syntaxInfo -> syntaxInfo.origin().name().equals("Lusk")).count() + classInfoAmount) + " elements:");
            getLogger().info(" - Types: " + classInfoAmount);
            for (var key : syntaxRegistryKeys) {
                long amount = syntaxRegistry.syntaxes(key).stream().filter(syntaxInfo -> syntaxInfo.origin().name().equals("Lusk")).count();
                getLogger().info(" - " + WordUtils.capitalizeFully(key.name()) + (amount == 1 ? "" : "s") + ": " + amount);
            }
            getLogger().info("Hooked with Skript!");
        }
        int pluginId = 17730;
        Metrics metrics = new Metrics(this, pluginId);
        rawVersion = getPluginMeta().getVersion();
        String buildType = getBuildType();

        ConsoleLogInjector.inject(l -> {
            boolean async = !getServer().isPrimaryThread();
            ConsoleLogEvent event = new ConsoleLogEvent(async, l.getMessageStrippedAnsi());
            getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) l.cancel();
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
        if (NMS != null)
            NMS = null;
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
}
