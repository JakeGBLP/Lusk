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
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Cancel Consuming Brewing Fuel")
@Description("Sets whether the fuel of the brewing stand in the Brewing Stand Fuel Event will be reduced / consumed.")
@Examples({"""
on brewing stand fuel:
  cancel brewing fuel consume
  broadcast "Infinite brewing fuel!\""""})
@Since("1.0.2")
public class EffCancelConsumingBrewingFuel extends Effect {
    static {
        Skript.registerEffect(EffCancelConsumingBrewingFuel.class, "[:un]cancel brewing fuel consume");
    }

    private boolean uncancel;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(BrewingStandFuelEvent.class)) {
            Skript.error("This effect can only be used in the Brewing Stand Fuel event!");
            return false;
        }
        uncancel = parser.hasTag("un");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (uncancel ? "un" : "") + "cancel brewing fuel consume";
    }

    @Override
    protected void execute(@NotNull Event event) {
        ((BrewingStandFuelEvent) event).setConsuming(uncancel);
    }
}