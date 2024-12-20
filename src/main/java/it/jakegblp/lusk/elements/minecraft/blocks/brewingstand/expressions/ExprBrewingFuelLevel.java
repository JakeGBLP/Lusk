package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.expressions;

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
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Brewing - Fuel Level")
@Description("Returns the brewing fuel level of a Brewing Stand.\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing fuel of event-block"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprBrewingFuelLevel extends SimpleExpression<Integer> {
    static {
        // todo: simple property expression, plural, utils
        Skript.registerExpression(ExprBrewingFuelLevel.class, Integer.class, ExpressionType.PROPERTY,
                "[the] brewing fuel level of %block%",
                "%block%'[s] brewing fuel level");
    }

    private Expression<Block> blockExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        Block block = blockExpression.getSingle(e);
        if (block != null && block.getState() instanceof BrewingStand brewingStand) {
            return new Integer[]{brewingStand.getFuelLevel()};
        }
        return new Integer[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Integer.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Integer integer) {
            Block block = blockExpression.getSingle(e);
            if (block != null && block.getState() instanceof BrewingStand brewingStand) {
                brewingStand.setFuelLevel(integer);
                brewingStand.update(true);
            }
        }
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
        return "the brewing fuel level of " + blockExpression.toString(e, debug);
    }
}
