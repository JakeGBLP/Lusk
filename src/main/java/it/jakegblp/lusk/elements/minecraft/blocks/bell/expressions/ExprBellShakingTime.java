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

@Name("Bell - Shaking Time")
@Description("`DEPRECATED SINCE SKRIPT 2.9`\nReturns the time since the bell has been shaking.")
@Examples({"on bell ring:\n\twait 5 seconds\n\tbroadcast shaking time of event-block"})
@Since("1.0.3, 1.2 (Deprecated)")
public class ExprBellShakingTime extends SimplePropertyExpression<Block, Timespan> {
    static {
        if (!Utils.SKRIPT_2_9) {
            register(ExprBellShakingTime.class, Timespan.class, "shaking time", "blocks");
        }
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