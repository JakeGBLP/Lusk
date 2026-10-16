package it.jakegblp.lusk.skript.modules.base.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Make Empty Effect Play")
@Description("Sets whether the empty dispense effect should play in the failed dispense event.")
@Example("""
        on failed dispense:
          make the empty effect not play""")
@Since("1.0.0")
public class EffCancelPlayEmptyEffect extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffCancelPlayEmptyEffect.class, EffCancelPlayEmptyEffect::new)
                .patterns("make the empty effect play", "make the empty effect not play")
                .register(syntaxRegistry);
    }

    private boolean not;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(BlockFailedDispenseEvent.class)) {
            Skript.error("This effect can only be used in the Failed Dispense Event!");
            return false;
        }
        not = matchedPattern == 1;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make the empty effect" + (not ? " not" : "") + " play";
    }

    @Override
    protected void execute(@NotNull Event event) {
        ((BlockFailedDispenseEvent) event).shouldPlayEffect(!not);
    }
}