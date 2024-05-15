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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Name("Damage Section")
@Description("""
        "Runs the code inside of it when the provided entity takes damage.
        Local Variables that are:
        - defined BEFORE this section CAN be used inside of it.
        - defined AFTER this section CANNOT be used inside of it.
        - defined INSIDE this section CANNOT be used outside of it.
        """)
@Examples(
        """
                command /a:
                  trigger:
                    spawn pig at player:
                      set display name of entity to "&dPig &7- &c%health of entity%&8/&c%max health of entity%"
                      on damage of entity:
                        set display name of victim to "&dPig &7- &c%health of victim - final damage%&8/&c%max health of victim%"
                """
)
@Since("1.1")
public class SecEvtDamage extends Section {
    public static class DamageListener implements Listener {
        static {
            Lusk.getInstance().registerListener(new DamageListener());
        }

        private static final HashMap<Entity, Consumer<Pair<EntityDamageByEntityEvent, Entity>>> map = new HashMap<>();

        private static void log(Consumer<Pair<EntityDamageByEntityEvent, Entity>> consumer, Entity entity) {
            map.put(entity, consumer);
        }

        @EventHandler
        public static void onEntityDamage(EntityDamageByEntityEvent event) {
            Entity entity = event.getEntity();
            if (map.containsKey(entity)) map.get(entity).accept(new Pair<>(event, entity));
        }
    }

    static {
        Skript.registerSection(SecEvtDamage.class, "[on] damage of %~entity%", "when %~entity% get[s] damaged");
    }

    private Expression<Entity> victim;
    @Nullable
    private Trigger trigger;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> list) {
        victim = (Expression<Entity>) (expressions[0]);
        trigger = loadCode(sectionNode, "damage", EntityDamageByEntityEvent.class);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        Consumer<Pair<EntityDamageByEntityEvent, Entity>> consumer = trigger == null ? null : o -> {
            EntityDamageByEntityEvent damageEvent = o.getFirst();
            if (damageEvent != null) {
                Variables.setLocalVariables(damageEvent, Variables.copyLocalVariables(event));
                TriggerItem.walk(trigger, damageEvent);
            }
        };
        DamageListener.log(consumer, victim.getSingle(event));
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when " + (event != null ? victim.toString(event, b) : "") + " gets damaged";
    }
}