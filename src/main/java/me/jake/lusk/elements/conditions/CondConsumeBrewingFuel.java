package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Brewing Stand - Will Consume Fuel")
@Description("Checks whether or not the brewing stand's fuel will be consumed in a Brewing Stand Fuel Event")
@Examples({"""
        on brewing stand fuel:
          # checks if the fuel will be consumed, it will by default
          if the brewing fuel will be consumed:
            # cancels it
            cancel brewing fuel consume
        """})
@Since("1.0.2")
public class CondConsumeBrewingFuel extends Condition {
    static {
        Skript.registerCondition(CondConsumeBrewingFuel.class, "[the] [brewing] fuel will be consumed",
                "[the] [brewing] fuel w(ill not|on't) be consumed");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(BrewingStandFuelEvent.class))) {
            Skript.error("This condition can only be used in the Brewing Stand Fuel event!");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the brewing fuel will " + (isNegated() ? "not" : "") + " be consumed";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ ((BrewingStandFuelEvent) event).isConsuming();
    }
}