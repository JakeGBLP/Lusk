package it.jakegblp.lusk;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.jakegblp.lusk.utils.Constants;
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

import static it.jakegblp.lusk.utils.LuskUtils.*;

/*
 * @author ShaneBeee, JakeGBLP
 */
@SuppressWarnings("deprecation")
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
        checkUpdate();
        setupJoinListener();
    }

    private void setupJoinListener() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            private void onJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                if (!player.hasPermission("lusk.update.check")) return;

                Bukkit.getScheduler().runTaskLater(UpdateChecker.this.plugin, () -> getUpdateVersion(true).thenApply(version -> {
                    send(player,"Lusk is &c&lOUTDATED&f!");
                    send(player,"New version: {0}", version);
                    send(player,"Download at: https://github.com/JakeGBLP/Lusk/releases");
                    return true;
                }), 30);
            }
        }, this.plugin);
    }

    private void checkUpdate() {
        consoleLog("&oChecking for update...");
        getUpdateVersion(false).thenApply(version -> {
            consoleLog("&cLusk is not up to date!");
            if (Constants.VERSION_SERVER.isGreaterThan(Constants.VERSION_SERVER_NEWEST_SUPPORTED)) {
                consoleLog("&cYou're running Minecraft {0} which is not supported by Lusk {1}, please update.", Constants.VERSION_SERVER, this.pluginVersion);
            }
            consoleLog("&a&lUpdate Available:");
            consoleLog(" &l»&c   {0} &7→&a {1}",this.pluginVersion,version);
            consoleLog(" &l»&7 Download at: https://github.com/JakeGBLP/Lusk/releases");
            return true;
        }).exceptionally(throwable -> {
            consoleLog("&aLusk is up to date!");
            if (Constants.VERSION_SERVER.isLowerThan(Constants.VERSION_SERVER_OLDEST_SUPPORTED)) {
                warning("You're running Minecraft {0} which is not supported by Lusk {1}, stability is not guaranteed.", Constants.VERSION_SERVER, this.pluginVersion);
            }
            if (Constants.VERSION_SKRIPT.isLowerThan(Constants.VERSION_SKRIPT_OLDEST_SUPPORTED)) {
                warning("You're running Skript {0} which is not supported by Lusk {1}, stability is not guaranteed.", Constants.VERSION_SKRIPT, this.pluginVersion);
            }
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
            consoleLog("&cChecking for update failed!");
        }
        return null;
    }
}