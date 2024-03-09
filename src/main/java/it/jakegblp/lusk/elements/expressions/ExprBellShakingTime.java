package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
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
public class ExprBellShakingTime extends SimplePropertyExpression<Block, Timespan> {
    static {
        register(ExprBellShakingTime.class, Timespan.class, "shaking time", "blocks");
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    @Nullable
    public Timespan convert(Block block) {
        return block.getState() instanceof Bell bell ? Timespan.fromTicks(bell.getShakingTicks()) : null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "shaking time";
    }
}