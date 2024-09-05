package it.jakegblp.lusk;

import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/*
 * @author ShaneBeee, JakeGBLP
 */
public class UpdateChecker implements Listener {

    private static Version UPDATE_VERSION;

    public static void checkForUpdate(String pluginVersion) {
        Lusk.getInstance().getLogger().info("Checking for updates.");
        getLatestReleaseVersion(version -> {
            Version plugVer = new Version(pluginVersion);
            Version curVer = new Version(version);
            if (curVer.compareTo(plugVer) <= 0) {
                Lusk.getInstance().getLogger().info("Lusk is up to date!");
            } else {
                Lusk.getInstance().getLogger().info("Lusk is NOT up to date!");
                Lusk.getInstance().getLogger().info("> Current Version: " + pluginVersion);
                Lusk.getInstance().getLogger().info("> Latest Version: " + version);
                Lusk.getInstance().getLogger().info("> Download it at: https://github.com/JakeGBLP/Lusk/releases");
                UPDATE_VERSION = curVer;
            }
        });
    }

    private static void getLatestReleaseVersion(final Consumer<String> consumer) {
        try {
            URL url = URL.of(URI.create("https://api.github.com/repos/JakeGBLP/Lusk/releases/latest"), null);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            String tag_name = jsonObject.get("tag_name").getAsString();
            consumer.accept(tag_name);
        } catch (IOException e) {
            Lusk.getInstance().getLogger().info("Update checker has failed...");
            e.printStackTrace();
        }
    }

    private final Lusk plugin;

    public UpdateChecker(Lusk plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("lusk.update.check") || !player.isOp()) return;

        String currentVersion = this.plugin.getDescription().getVersion();
        CompletableFuture<Version> updateVersion = getUpdateVersion(currentVersion);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> updateVersion.thenApply(version -> {
            player.sendRichMessage("<red>[<white>Lusk<red>] <white>Lusk is <red><bold>OUTDATED<white>!");
            player.sendRichMessage("<red>[<white>Lusk<red>] <white>New version: " + version);
            player.sendRichMessage("<red>[<white>Lusk<red>] <white>Download at: <link>https://github.com/JakeGBLP/Lusk/releases");
            return true;
        }), 30);
    }

    private CompletableFuture<Version> getUpdateVersion(String currentVersion) {
        CompletableFuture<Version> future = new CompletableFuture<>();
        if (UPDATE_VERSION != null) {
            future.complete(UPDATE_VERSION);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> getLatestReleaseVersion(version -> {
                Version plugVer = new Version(currentVersion);
                Version curVer = new Version(version);
                if (curVer.compareTo(plugVer) <= 0) {
                    future.cancel(true);
                } else {
                    UPDATE_VERSION = curVer;
                    future.complete(UPDATE_VERSION);
                }
            }));
        }
        return future;
    }
}