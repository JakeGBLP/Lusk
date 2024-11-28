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
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.DeprecationUtils.fromTicks;
import static it.jakegblp.lusk.utils.DeprecationUtils.getTicks;

@Name("Brewing - Time")
@Description("Returns the brewing time of a Brewing Stand (the time before the brewing is over, 0 seconds = finished, 20 seconds = just started. Can be set to a longer time, progress won't be displayed until it reaches 20 seconds).\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing time of event-block"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprBrewingTime extends SimpleExpression<Timespan> {
    static {
        // todo: simple property expression, plural, utils
        Skript.registerExpression(ExprBrewingTime.class, Timespan.class, ExpressionType.PROPERTY,
                "[the] brewing time of %block%",
                "%block%'[s] brewing time");
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
                return new Timespan[]{fromTicks(brewingStand.getBrewingTime())};
            }
        }
        return new Timespan[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return (mode == Changer.ChangeMode.SET) ? new Class[]{Timespan.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Timespan timespan) {
            Block block = blockExpression.getSingle(e);
            if (block != null && block.getState() instanceof BrewingStand brewingStand) {
                brewingStand.setBrewingTime(((int) getTicks(timespan)));
                brewingStand.update(true);
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
        return "the brewing time of " + blockExpression.toString(e, debug);
    }
}
