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
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Beacon - Cancel Consuming Item")
@Description("Sets whether the item in the Beacon Effect Change Event will be consumed.")
@Examples({"""
        on beacon effect change:
          cancel beacon item consume"""})
@Since("1.0.4")
public class EffCancelConsumingBeaconItem extends Effect {
    static {
        Skript.registerEffect(EffCancelConsumingBeaconItem.class, "cancel beacon item consume", "uncancel beacon item consume");
    }

    private boolean negated;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(PlayerChangeBeaconEffectEvent.class)) {
            Skript.error("This effect can only be used in the Beacon Effect Change event!");
            return false;
        }
        negated = matchedPattern == 1;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (negated ? "un" : "") + "cancel beacon item consume";
    }

    @Override
    protected void execute(@NotNull Event event) {
        ((PlayerChangeBeaconEffectEvent) event).setConsumeItem(negated);
    }
}