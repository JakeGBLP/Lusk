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
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Should Play The Empty Effect")
@Description("Checks whether the block should play an effect when empty in the failed dispense event.")
@Example("""
        on dispense fail:
          if the empty effect should be played:
            cancel composter rise
        """)
@Since("1.0.0")
public class CondShouldPlayEmptyEffect extends Condition {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.condition(CondShouldPlayEmptyEffect.class, CondShouldPlayEmptyEffect::new)
                .patterns("[the] empty effect should be played", "[the] empty effect should(n't| not) be played")
                .register(syntaxRegistry);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parser) {
        if (!(getParser().isCurrentEvent(BlockFailedDispenseEvent.class))) {
            Skript.error("This condition can only be used in the failed dispense event!");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the empty effect should " + (isNegated() ? "not" : "") + " be played";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return event instanceof BlockFailedDispenseEvent blockFailedDispenseEvent && isNegated() ^ blockFailedDispenseEvent.shouldPlayEffect();
    }
}