package me.jake.lusk.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.block.CompostItemEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Cancel Composter Level Rise")
@Description("This Effect requires Paper.\n\nCan only be used in a Compost Item Event.\nSets whether or not the composter's level should rise.")
@Examples({"""
           on hopper compost:
             cancel composter level rise"""})
@Since("1.0.0")
public class EffCancelComposterRising extends Effect {
    static {
        Skript.registerEffect(EffCancelComposterRising.class, "cancel [the] composter [level] rise", "uncancel [the] composter [level] rise");
    }

    private boolean negated;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(CompostItemEvent.class)) {
            Skript.error("This effect can only be used in the Compost Item Event!");
            return false;
        }
        negated = matchedPattern == 1;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (negated ? "un" : "") + "cancel the composter level raise";
    }

    @Override
    protected void execute(@NotNull Event event) {
        ((CompostItemEvent)event).setWillRaiseLevel(negated);
    }
}