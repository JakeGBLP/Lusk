package me.jake.lusk.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ComponentClickRun implements Listener {

    private final Map<String, Consumer<PlayerCommandPreprocessEvent>> functions = new HashMap<>();

    public ComponentClickRun() {
    }

    public String createCommand(String identifier, Consumer<PlayerCommandPreprocessEvent> function) {
        functions.put(identifier, function);
        return "/" + identifier;
    }

    public String createCommand(Consumer<PlayerCommandPreprocessEvent> function) {
        return createCommand("✅component-click-" + UUID.randomUUID(), function);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1);

        Consumer<PlayerCommandPreprocessEvent> function = functions.get(command);

        if (function != null) {
            event.setCancelled(true);
            function.accept(event);
        }
    }
}