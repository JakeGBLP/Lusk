package it.jakegblp.lusk.api.listeners;

import it.jakegblp.lusk.api.events.AnvilGuiOpenEvent;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AnvilGuiOpenListener implements Listener {

    private static final Map<AnvilGuiWrapper, Consumer<AnvilGuiOpenEvent>> SECTIONS = new HashMap<>();

    /**
     * Logs a Consumer along with its AnvilGuiWrapper.
     *
     * @param consumer        The consumer containing the section's contents and the variable management.
     * @param anvilGuiWrapper The entity involved at runtime.
     */
    public static void log(Consumer<AnvilGuiOpenEvent> consumer, AnvilGuiWrapper anvilGuiWrapper) {
        SECTIONS.put(anvilGuiWrapper, consumer);
    }

    public static void remove(AnvilGuiWrapper anvilGuiWrapper) {
        SECTIONS.remove(anvilGuiWrapper);
    }

    @EventHandler
    public static void onEntityDamage(AnvilGuiOpenEvent event) {
        AnvilGuiWrapper anvilGuiWrapper = event.getAnvil();
        if (SECTIONS.containsKey(anvilGuiWrapper)) SECTIONS.get(anvilGuiWrapper).accept(event);
    }
}