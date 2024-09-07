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
import it.jakegblp.lusk.api.listeners.HealListener;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@Name("Entity - on Heal Section")
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

    static {
        Skript.registerSection(SecEvtHeal.class, "[execute|run] on heal of %~entity%", "[execute|run] when %~entity% get[s] healed");
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
        Object vars = Variables.copyLocalVariables(event);
        Consumer<EntityRegainHealthEvent> consumer = trigger == null ? null : healthEvent -> {
            Variables.setLocalVariables(healthEvent, vars);
            TriggerItem.walk(trigger, healthEvent);
            Variables.removeLocals(healthEvent);

        };
        HealListener.log(consumer, entity.getSingle(event));
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when " + (event != null ? entity.toString(event, b) : "") + " gets healed";
    }
}