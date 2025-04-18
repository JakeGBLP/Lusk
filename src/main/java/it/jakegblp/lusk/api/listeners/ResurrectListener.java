package it.jakegblp.lusk.api.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ResurrectListener implements Listener {

    private static final Map<Entity, Consumer<EntityResurrectEvent>> SECTIONS = new HashMap<>();

    /**
     * Logs a Consumer along with its Entity.
     *
     * @param consumer The consumer containing the section's contents and the variable management.
     * @param entity   The entity involved at runtime.
     */
    public static void log(Consumer<EntityResurrectEvent> consumer, Entity entity) {
        SECTIONS.put(entity, consumer);
    }

    public static void remove(Entity entity) {
        SECTIONS.remove(entity);
    }

    @EventHandler
    public static void onEntityResurrect(EntityResurrectEvent event) {
        Entity entity = event.getEntity();
        if (SECTIONS.containsKey(entity)) SECTIONS.get(entity).accept(event);
    }
}