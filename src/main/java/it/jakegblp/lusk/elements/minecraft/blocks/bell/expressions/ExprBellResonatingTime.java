package it.jakegblp.lusk.elements.minecraft.blocks.bell.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.utils.Utils;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bell - Resonating Time")
@Description("""
        `DEPRECATED SINCE SKRIPT 2.9`
        Returns the time since the bell has been resonating, or 0 seconds if the bell is not currently resonating.
        A bell will typically resonate for 40 ticks (2 seconds)""")
@Examples({"on bell ring:\n\twait 5 seconds\n\tbroadcast resonating time of event-block"})
@Since("1.0.3, 1.2 (Deprecated)")
public class ExprBellResonatingTime extends SimplePropertyExpression<Block, Timespan> {
    static {
        if (!Utils.SKRIPT_2_9) {
            register(ExprBellResonatingTime.class, Timespan.class, "resonating time", "blocks");
        }
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    @Nullable
    public Timespan convert(Block block) {
        return block.getState() instanceof Bell bell ? Timespan.fromTicks(bell.getResonatingTicks()) : null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "resonating time";
    }
}