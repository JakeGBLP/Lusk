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
import it.jakegblp.lusk.api.listeners.DamageListener;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@Name("Entity - on Damage Section")
@Description("""
        Runs the code inside of it when the provided entity takes damage.

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

    static {
        Skript.registerSection(SecEvtDamage.class, "[execute|run] on damage of %~entity%", "[execute|run] when %~entity% (get[s] damaged|take[s] damage)");
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
        Object vars = Variables.copyLocalVariables(event);
        Consumer<EntityDamageByEntityEvent> consumer = trigger == null ? null : damageEvent -> {
            Variables.setLocalVariables(damageEvent, vars);
            TriggerItem.walk(trigger, damageEvent);
            Variables.removeLocals(damageEvent);
        };
        DamageListener.log(consumer, victim.getSingle(event));
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when " + (event != null ? victim.toString(event, b) : "") + " gets damaged";
    }
}