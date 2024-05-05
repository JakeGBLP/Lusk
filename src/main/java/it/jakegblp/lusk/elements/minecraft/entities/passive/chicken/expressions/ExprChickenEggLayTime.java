package it.jakegblp.lusk.elements.minecraft.entities.passive.chicken.expressions;

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
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Chicken - Egg Lay Time")
@Description("Returns the time till a chicken lays an egg.")
@Examples({"set egg lay time of target to 2 minutes"})
@Since("1.0.3")
public class ExprChickenEggLayTime extends SimpleExpression<Timespan> {
    static {
        Skript.registerExpression(ExprChickenEggLayTime.class, Timespan.class, ExpressionType.COMBINED,
                "[the] [chicken] egg lay time of %livingentity%",
                "%livingentity%'[s] [chicken] egg lay time");
    }

    private Expression<LivingEntity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        entityExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }

    @Override
    protected Timespan @NotNull [] get(@NotNull Event e) {
        LivingEntity entity = entityExpression.getSingle(e);
        if (entity instanceof Chicken chicken) {
            return new Timespan[]{Timespan.fromTicks(chicken.getEggLayTime())};
        }
        return new Timespan[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Timespan.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Timespan timespan)
            if (entityExpression.getSingle(e) instanceof Chicken chicken)
                chicken.setEggLayTime((int) timespan.getTicks());
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
        return "the chicken egg lay time of " + (e == null ? "" : entityExpression.toString(e, debug));
    }
}
