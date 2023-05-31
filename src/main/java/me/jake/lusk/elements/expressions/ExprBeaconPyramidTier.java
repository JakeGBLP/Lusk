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
import ch.njol.util.Kleenean;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - Pyramid Tier")
@Description("Returns pyramid tier of a beacon.")
@Examples({"broadcast the pyramid tier of {_beacon}"})
@Since("1.0.3")
public class ExprBeaconPyramidTier extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprBeaconPyramidTier.class, Integer.class, ExpressionType.SIMPLE,
                "[the] (beacon|pyramid) tier of %block%",
                "%block%'[s] (beacon|pyramid) tier");
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
            if (block.getState() instanceof Beacon beacon) {
                return new Integer[]{beacon.getTier()};
            }
        }
        return new Integer[0];
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
        return "the pyramid tier of " + (e == null ? "" : blockExpression.getSingle(e));
    }
}
