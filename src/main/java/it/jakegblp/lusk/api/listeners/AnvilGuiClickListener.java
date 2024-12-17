package it.jakegblp.lusk.api.listeners;

import it.jakegblp.lusk.api.events.AnvilGuiClickEvent;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AnvilGuiClickListener implements Listener {

    private static final Map<AnvilGuiWrapper, Consumer<AnvilGuiClickEvent>> SECTIONS = new HashMap<>();

    /**
     * Logs a Consumer along with its AnvilGuiWrapper.
     *
     * @param consumer        The consumer containing the section's contents and the variable management.
     * @param anvilGuiWrapper The entity involved at runtime.
     */
    public static void log(Consumer<AnvilGuiClickEvent> consumer, AnvilGuiWrapper anvilGuiWrapper) {
        SECTIONS.put(anvilGuiWrapper, consumer);
    }

    public static void remove(AnvilGuiWrapper anvilGuiWrapper) {
        SECTIONS.remove(anvilGuiWrapper);
    }

    @EventHandler
    public static void onEntityDamage(AnvilGuiClickEvent event) {
        AnvilGuiWrapper anvilGuiWrapper = event.getAnvil();
        if (SECTIONS.containsKey(anvilGuiWrapper)) SECTIONS.get(anvilGuiWrapper).accept(event);
    }
}