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
import it.jakegblp.lusk.api.listeners.ResurrectListener;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

@Name("Entity - on Resurrect Section")
@Description("""
        Runs the code inside of it when the provided entity dies and may have the opportunity to be resurrected.
        Will be called in a cancelled state if the entity does not have a totem equipped.

        Local Variables that are:
        - defined BEFORE this section CAN be used inside of it.
        - defined AFTER this section CANNOT be used inside of it.
        - defined INSIDE this section CANNOT be used outside of it.
        """)
@Examples(
        """
        command /a:
          trigger:
            on resurrection of player:
              uncancel event
        """
)
@Since("1.3.1")
public class SecEvtResurrect extends Section {

    static {
        Skript.registerSection(SecEvtResurrect.class, "[execute|run] on [entity] resurrect[ion] [attempt] of %~entity%", "[execute|run] when %~entity% (resurrect[s]|attempt[s] to resurrect)");
    }

    private Expression<Entity> entity;
    @Nullable
    private Trigger trigger;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> list) {
        entity = (Expression<Entity>) (expressions[0]);
        trigger = loadCode(sectionNode, "resurrect", EntityResurrectEvent.class);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        Object vars = Variables.copyLocalVariables(event);
        Consumer<EntityResurrectEvent> consumer = trigger == null ? null : resurrectEvent -> {
            Variables.setLocalVariables(resurrectEvent, vars);
            TriggerItem.walk(trigger, resurrectEvent);
            Variables.removeLocals(resurrectEvent);
        };
        ResurrectListener.log(consumer, entity.getSingle(event));
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when " + entity.toString(event, b) + " resurrect";
    }
}