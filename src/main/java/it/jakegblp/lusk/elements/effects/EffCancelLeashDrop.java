package it.jakegblp.lusk.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Leash - Cancel Drop")
@Description("Sets whether the leash in the Unleash Event will be dropped.")
@Examples({"""
        on unleash:
          cancel leash drop"""})
@Since("1.0.4")
public class EffCancelLeashDrop extends Effect {
    static {
        Skript.registerEffect(EffCancelLeashDrop.class, "cancel leash drop", "uncancel leash drop");
    }

    private boolean negated;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(PlayerUnleashEntityEvent.class)) {
            Skript.error("This effect can only be used in the Unleash Event!");
            return false;
        }
        negated = matchedPattern == 1;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (negated ? "un" : "") + "cancel leash drop";
    }

    @Override
    protected void execute(@NotNull Event event) {
        ((PlayerUnleashEntityEvent) event).setDropLeash(negated);
    }
}