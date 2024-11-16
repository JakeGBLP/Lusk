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

import static it.jakegblp.lusk.utils.Constants.SKRIPT_2_9;

@Name("Bell - Resonating Time")
@Description("""
        `DEPRECATED SINCE SKRIPT 2.9`
        Returns the time since the bell has been resonating, or 0 seconds if the bell is not currently resonating.
        A bell will typically resonate for 40 ticks (2 seconds)""")
@Examples({"on bell ring:\n\twait 5 seconds\n\tbroadcast resonating time of event-block"})
@Since("1.0.3, 1.2 (Deprecated)")
@SuppressWarnings("unused")
public class ExprBellResonatingTime extends SimplePropertyExpression<Block, Object> {
    static {
        // todo: undeprecate?
        if (!SKRIPT_2_9) {
            register(ExprBellResonatingTime.class, Object.class, "resonating (time|ticks)", "blocks");
        }
    }

    boolean ticks;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        ticks = parseResult.hasTag("ticks");
        return true;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    @Nullable
    public Object convert(Block block) {
        return block.getState() instanceof Bell bell ? (ticks ? bell.getResonatingTicks() : Timespan.fromTicks(bell.getResonatingTicks())) : null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "resonating time";
    }
}