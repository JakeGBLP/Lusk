package it.jakegblp.lusk.elements.minecraft.entities.entity.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import ch.njol.util.Pair;
import it.jakegblp.lusk.Lusk;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Name("Heal Section")
@Description("""
        Runs the code inside of it when the provided entity gets healed.
        Local Variables that are:
        - defined BEFORE this section CAN be used inside of it.
        - defined AFTER this section CANNOT be used inside of it.
        - defined INSIDE this section CANNOT be used outside of it.
        """)
@Examples("""
        command /a:
          trigger:
            spawn pig at player:
              set display name of entity to "&dPig &7- &c%health of entity%&8/&c%max health of entity%"
              on heal of entity:
                push entity upwards
                broadcast "%entity% has been healed, yay"
        """)
@Since("1.1")
public class SecEvtHeal extends Section {
    public static class HealListener implements Listener {
        static {
            Lusk.getInstance().registerListener(new HealListener());
        }

        private static final HashMap<Entity, Consumer<Pair<EntityRegainHealthEvent, Object>>> map = new HashMap<>();

        private static void log(Consumer<Pair<EntityRegainHealthEvent, Object>> consumer, Entity entity) {
            map.put(entity, consumer);
        }

        @EventHandler
        public static void onEntityHeal(EntityRegainHealthEvent event) {
            Entity entity = event.getEntity();
            if (map.containsKey(entity)) map.get(entity).accept(new Pair<>(event, entity));
        }
    }

    static {
        Skript.registerSection(SecEvtHeal.class, "[on] heal of %~entity%", "when %~entity% get[s] healed");
    }

    private Expression<Entity> entity;
    @Nullable
    private Trigger trigger;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> list) {
        entity = (Expression<Entity>) (expressions[0]);
        trigger = loadCode(sectionNode, "heal", EntityRegainHealthEvent.class);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        Consumer<Pair<EntityRegainHealthEvent, Object>> consumer = trigger == null ? null : o -> {
            EntityRegainHealthEvent healthEvent = o.getFirst();
            if (healthEvent != null) {
                Variables.setLocalVariables(healthEvent, Variables.copyLocalVariables(event));
                TriggerItem.walk(trigger, healthEvent);
            }
        };
        HealListener.log(consumer, entity.getSingle(event));
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when " + (event != null ? entity.toString(event, b) : "") + " gets healed";
    }
}