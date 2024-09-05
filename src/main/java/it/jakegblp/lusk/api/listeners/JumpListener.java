package it.jakegblp.lusk.api.listeners;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.api.events.GenericEntityJumpEvent;
import it.jakegblp.lusk.utils.PaperUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.HorseJumpEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class JumpListener implements Listener {

    private static final Map<Entity, Consumer<GenericEntityJumpEvent>> SECTIONS = new HashMap<>();

    /**
     * Checks if an entity can be logged for the jump listener.
     *
     * @param entity The entity to check for logging.
     * @return True, if both paper jump events are present or if the entity is a horse, else false.
     */
    public static boolean canLog(Entity entity) {
        if (PaperUtils.HAS_PLAYER_JUMP_EVENT && PaperUtils.HAS_ENTITY_JUMP_EVENT) return true;
        return entity instanceof Horse;
    }

    /**
     * Logs a Consumer along with its Entity if {@link JumpListener#canLog(Entity)} returns true.
     *
     * @param consumer The consumer containing the section's contents and the variable management.
     * @param entity   The entity involved at runtime.
     * @see JumpListener#canLog(Entity)
     */
    public static void log(Consumer<GenericEntityJumpEvent> consumer, Entity entity) {
        if (canLog(entity)) SECTIONS.put(entity, consumer);
    }

    public static void remove(Entity entity) {
        SECTIONS.remove(entity);
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
        public void onEntityJump(EntityJumpEvent event) {
            Lusk.callEvent(new GenericEntityJumpEvent(event.getEntity()));
        }

        @EventHandler
        public static void onPlayerJump(PlayerJumpEvent event) {
            Lusk.callEvent(new GenericEntityJumpEvent(event.getPlayer()));
        }
    }

    @EventHandler
    public static void onGenericEntityJump(GenericEntityJumpEvent event) {
        Entity entity = event.getEntity();
        if (SECTIONS.containsKey(entity)) SECTIONS.get(entity).accept(event);
    }
}
