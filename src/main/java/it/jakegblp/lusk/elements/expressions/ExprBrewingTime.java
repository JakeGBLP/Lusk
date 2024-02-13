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
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Brewing - Time")
@Description("Returns the brewing time of a Brewing Stand (the time before the brewing is over, 0 seconds = finished, 20 seconds = just started. Can be set to a longer time, progress won't be displayed until it reaches 20 seconds).\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing time of event-block"})
@Since("1.0.2")
public class ExprBrewingTime extends SimpleExpression<Timespan> {
    static {
        Skript.registerExpression(ExprBrewingTime.class, Timespan.class, ExpressionType.COMBINED,
                "[the] brewing time of %block%");
    }

    private Expression<Block> blockExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    protected Timespan @NotNull [] get(@NotNull Event e) {
        Block block = blockExpression.getSingle(e);
        if (block != null) {
            BlockState blockState = block.getState();
            if (blockState instanceof BrewingStand brewingStand) {
                return new Timespan[]{Timespan.fromTicks(brewingStand.getBrewingTime())};
            }
        }
        return new Timespan[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Timespan[].class);
        } else {
            return new Class[0];
        }
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Timespan timespan = delta instanceof Timespan[] ? ((Timespan[]) delta)[0] : null;
        if (timespan == null) return;
        Block block = blockExpression.getSingle(e);
        if (block != null) {
            BlockState blockState = block.getState();
            if (blockState instanceof BrewingStand brewingStand) {
                brewingStand.setBrewingTime(((int) timespan.getTicks()));
                brewingStand.update();
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the brewing time of " + (e == null ? "" : blockExpression.getSingle(e));
    }
}
