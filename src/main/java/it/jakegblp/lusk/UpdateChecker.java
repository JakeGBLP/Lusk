package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static it.jakegblp.lusk.utils.LuskUtils.consoleLog;
import static it.jakegblp.lusk.utils.LuskUtils.send;

/*
 * @author ShaneBeee, JakeGBLP
 */
public class UpdateChecker implements Listener {

    public static final Function<String, URL> GET_URL_METHOD = Skript.methodExists(URL.class, "of", URI.class, URLStreamHandler.class) ? string -> {
        try {
            return URL.of(URI.create(string), null);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    } : string -> {
        try {
            return new URL(null,string);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    };

    private final Lusk plugin;
    private final Version pluginVersion;
    private Version currentUpdateVersion;

    public UpdateChecker(Lusk plugin) {
        this.plugin = plugin;
        this.pluginVersion = new Version(plugin.getDescription().getVersion());

        setupJoinListener();
    }

    private void setupJoinListener() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            private void onJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                if (!player.hasPermission("lusk.update.check")) return;

                Bukkit.getScheduler().runTaskLater(UpdateChecker.this.plugin, () -> getUpdateVersion(true).thenApply(version -> {
                    send(player,"Lusk is <red><bold>OUTDATED<white>!");
                    send(player,"New version: {0}", version);
                    send(player,"Download at: <link>https://github.com/JakeGBLP/Lusk/releases");
                    return true;
                }), 30);
            }
        }, this.plugin);
    }

    // what was this for?
    private void checkUpdate(boolean async) {
        consoleLog("Checking for update...");
        getUpdateVersion(async).thenApply(version -> {
            consoleLog("<red>Lusk is not up to date!");
            consoleLog(" - Current version: {0}",this.pluginVersion);
            consoleLog(" - Available update: {0}",version);
            consoleLog(" - Download available at: https://github.com/JakeGBLP/Lusk/releases");
            return true;
        }).exceptionally(throwable -> {
            consoleLog("<green>Lusk is up to date!");
            return true;
        });
    }

    private CompletableFuture<Version> getUpdateVersion(boolean async) {
        CompletableFuture<Version> future = new CompletableFuture<>();
        if (this.currentUpdateVersion != null) {
            future.complete(this.currentUpdateVersion);
        } else {
            getLatestReleaseVersion(async).thenApply(version -> {
                if (version.compareTo(this.pluginVersion) <= 0) {
                    future.cancel(true);
                } else {
                    this.currentUpdateVersion = version;
                    future.complete(this.currentUpdateVersion);
                }
                return true;
            });
        }
        return future;
    }

    private CompletableFuture<Version> getLatestReleaseVersion(boolean async) {
        CompletableFuture<Version> future = new CompletableFuture<>();
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                Version latest = getLatestVersionFromGitHub();
                if (latest == null) future.cancel(true);
                future.complete(latest);
            });
        } else {
            Version latest = getLatestVersionFromGitHub();
            if (latest == null) future.cancel(true);
            future.complete(latest);
        }
        return future;
    }

    private @Nullable Version getLatestVersionFromGitHub() {
        try {
            URL url = GET_URL_METHOD.apply("https://api.github.com/repos/JakeGBLP/Lusk/releases/latest");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            String tag_name = jsonObject.get("tag_name").getAsString();
            return new Version(tag_name);
        } catch (IOException e) {
            consoleLog("<red>Checking for update failed!");
        }
        return null;
    }
}