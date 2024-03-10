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
import ch.njol.util.Kleenean;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - Effect Range")
@Description("Returns effect range of a beacon.\nCan be set and reset.\nSetting it to a negative value will reset it.\nResetting it will set it to the default range based on the pyramid tier.")
@Examples({"broadcast the effect range of {_beacon}"})
@Since("1.0.3")
public class ExprBeaconEffectRange extends SimpleExpression<Double> {
    static {
        Skript.registerExpression(ExprBeaconEffectRange.class, Double.class, ExpressionType.SIMPLE,
                "[the] effect range of %block%",
                "%block%'[s] effect range");
    }

    private Expression<Block> blockExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    protected Double[] get(@NotNull Event e) {
        Block block = blockExpression.getSingle(e);
        if (block != null && block.getState() instanceof Beacon beacon) {
            return new Double[]{beacon.getEffectRange()};
        }
        return null;
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET ? new Class[]{Double.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Block block = blockExpression.getSingle(e);
        if (block == null) return;
        if (block.getState() instanceof Beacon beacon) {
            if (mode == Changer.ChangeMode.RESET) beacon.resetEffectRange();
            else if (delta[0] instanceof Double aDouble) beacon.setEffectRange(aDouble);
            beacon.update();
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the effect range of " + (e == null ? "" : blockExpression.toString(e,debug));
    }
}
