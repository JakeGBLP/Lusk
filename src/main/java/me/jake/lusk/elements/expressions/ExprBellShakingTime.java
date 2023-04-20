package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
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
import org.bukkit.block.Bell;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bell - Shaking Time")
@Description("Returns the time since the bell has been shaking.")
@Examples({"on bell ring:\n\twait 5 seconds\n\tbroadcast shaking time of event-block"})
@Since("1.0.3")
public class ExprBellShakingTime extends SimpleExpression<Timespan> {
    static {
        Skript.registerExpression(ExprBellShakingTime.class, Timespan.class, ExpressionType.COMBINED,
                "[the] shaking [time] of %block%");
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
        if (block instanceof Bell bell) {
            return new Timespan[]{Timespan.fromTicks_i(bell.getShakingTicks())};
        }
        return new Timespan[0];
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
        return "the shaking time of " + (e == null ? "" : blockExpression.getSingle(e));
    }
}