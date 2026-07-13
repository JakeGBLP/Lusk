package it.jakegblp.lusk.api.listeners;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.api.events.GenericEntityJumpEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.HorseJumpEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class JumpListener implements Listener {

    private static final Map<Entity, Consumer<GenericEntityJumpEvent>> SECTIONS = new HashMap<>();

    /**
     * @param consumer The consumer containing the section's contents and the variable management.
     * @param entity   The entity involved at runtime.
     */
    public static void log(Consumer<GenericEntityJumpEvent> consumer, Entity entity) {
        SECTIONS.put(entity, consumer);
    }

    public static void remove(Entity entity) {
        SECTIONS.remove(entity);
    }

    @EventHandler
    public static void onGenericEntityJump(GenericEntityJumpEvent event) {
        Entity entity = event.getEntity();
        if (SECTIONS.containsKey(entity)) SECTIONS.get(entity).accept(event);
    }

    public static class SpigotJumpListener implements Listener {

        @EventHandler
        public static void onHorseJump(HorseJumpEvent event) {
            Lusk.callEvent(new GenericEntityJumpEvent(event.getEntity()));
        }
        //@EventHandler
        //public static void onPlayerStatisticJump(PlayerStatisticIncrementEvent event) {
        //    if (event.getStatistic() == Statistic.JUMP) {
        //        Lusk.callEvent(new GenericEntityJumpEvent(event.getPlayer()));
        //    }
        //}
    }

    public static class PaperJumpListener implements Listener {
        @EventHandler
        public static void onPlayerJump(PlayerJumpEvent event) {
            Lusk.callEvent(new GenericEntityJumpEvent(event.getPlayer()));
        }

        @EventHandler
        public void onEntityJump(EntityJumpEvent event) {
            Lusk.callEvent(new GenericEntityJumpEvent(event.getEntity()));
        }
    }
}
