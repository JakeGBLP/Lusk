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
import io.papermc.paper.event.block.CompostItemEvent;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Cancel Composter Level Rise")
@Description("This Effect requires Paper.\n\nCan only be used in a Compost Item Event.\nSets whether or not the composter's level should rise.")
@Example("""
        on hopper compost:
          cancel composter level rise""")
@Since("1.0.0")
public class EffCancelComposterRising extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffCancelComposterRising.class, EffCancelComposterRising::new)
                .patterns("[:un]cancel [the] composter [level] rise")
                .register(syntaxRegistry);
    }

    private boolean uncancel;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(CompostItemEvent.class)) {
            Skript.error("This effect can only be used in the Item Compost Event!");
            return false;
        }
        uncancel = parser.hasTag("un");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (uncancel ? "un" : "") + "cancel the composter level raise";
    }

    @Override
    protected void execute(@NotNull Event event) {
        ((CompostItemEvent) event).setWillRaiseLevel(uncancel);
    }
}