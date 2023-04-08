package me.jake.lusk.elements.expressions;

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
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Brewing - Fuel Level")
@Description("Returns the brewing fuel level of a Brewing Stand.\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing fuel of event-block"})
@Since("1.0.2")
public class ExprBrewingFuelLevel extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprBrewingFuelLevel.class, Integer.class, ExpressionType.COMBINED,
                "[the] brewing fuel level of %block%");
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
        if (block != null) {
            BlockState blockState = block.getState();
            if (blockState instanceof BrewingStand brewingStand) {
                return new Integer[]{brewingStand.getFuelLevel()};
            }
        }
        return new Integer[0];
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
        Block block = blockExpression.getSingle(e);
        if (block != null) {
            BlockState blockState = block.getState();
            if (blockState instanceof BrewingStand brewingStand) {
                brewingStand.setFuelLevel(integer);
                brewingStand.update();
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
        return "the brewing fuel level of " + (e == null ? "" : blockExpression.getSingle(e));
    }
}
