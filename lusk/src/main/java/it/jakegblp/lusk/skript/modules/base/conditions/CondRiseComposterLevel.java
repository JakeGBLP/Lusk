package it.jakegblp.lusk.skript.modules.base.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.block.CompostItemEvent;
import io.papermc.paper.event.entity.EntityCompostItemEvent;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Will Raise Composter Level")
@Description("Checks whether the composter's level will rise in a Compost Item Event")
@Example("""
        on compost:
          if the composter level will be raised:
            cancel composter rise
        """)
@Since("1.0.0")
public class CondRiseComposterLevel extends Condition {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.condition(CondCanBeWaterLogged.class, CondCanBeWaterLogged::new)
                .patterns("[the] composter level will be raised", "[the] composter level w(ill no|on')t be raised")
                .register(syntaxRegistry);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parser) {
        if (!(getParser().isCurrentEvent(CompostItemEvent.class, EntityCompostItemEvent.class))) {
            Skript.error("This condition can only be used in the Hopper Compost event!");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the composter level will " + (isNegated() ? "not" : "") + " be raised";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return event instanceof CompostItemEvent compostItemEvent && isNegated() ^ compostItemEvent.willRaiseLevel();
    }
}