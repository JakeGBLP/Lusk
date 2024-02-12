package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Brewing Stand Fuel Power")
@Description("Returns the fuel power for the fuel in the Brewing Stand Fuel event.\n Can be set.")
@Examples({"on brewing stand fuel:\n\tbroadcast the brewing fuel power"})
@Since("1.0.2")
public class ExprBrewingFuelPower extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprBrewingFuelPower.class, Integer.class, ExpressionType.SIMPLE,
                "[the] [brewing] fuel power");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(BrewingStandFuelEvent.class)) {
            Skript.error("This expression can only be used in the Brewing Stand Fuel event!");
            return false;
        }
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        return new Integer[]{((BrewingStandFuelEvent) e).getFuelPower()};
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Integer[].class);
        } else {
            return new Class[0];
        }
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Integer integer = delta instanceof Integer[] ? ((Integer[]) delta)[0] : null;
        if (integer == null) return;
        ((BrewingStandFuelEvent) e).setFuelPower(integer);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the brewing fuel power";
    }
}
