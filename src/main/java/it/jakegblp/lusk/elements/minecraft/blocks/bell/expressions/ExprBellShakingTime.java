package it.jakegblp.lusk.elements.minecraft.blocks.bell.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bell - Shaking Time/Ticks")
@Description("""
        Returns the time or ticks since one or more bells have been shaking, or 0 seconds for each bell that's not currently resonating.
        A bell will typically resonate for 50 ticks (2.5 seconds)
        
        Note: if you wish to use this expression on Skript 2.9+ you will need to use Lusk 1.3+.
        """)
@Examples({"on bell ring:\n\twait 5 seconds\n\tbroadcast shaking time of event-block"})
@Since("1.0.3, 1.3 (ticks)")
@SuppressWarnings("unused")
public class ExprBellShakingTime extends SimplePropertyExpression<Block, Object> {
    static {
        register(ExprBellShakingTime.class, Object.class, "shaking (time|ticks)", "blocks");
    }

    boolean useTicks;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        useTicks = parseResult.hasTag("ticks");
        return true;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    @Nullable
    public Object convert(Block block) {
        // todo: make all deprecated timespan method calls safe
        if (block.getState() instanceof Bell bell) {
            int ticks = bell.getShakingTicks();
            if (useTicks) return ticks;
            else return Timespan.fromTicks(ticks);
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "shaking " + (useTicks ? "ticks" : "time");
    }
}