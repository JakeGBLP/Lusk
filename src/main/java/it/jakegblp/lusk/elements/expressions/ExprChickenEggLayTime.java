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
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
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
                "[the] [chicken] egg lay time of %livingentity%");
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
            return new Timespan[]{Timespan.fromTicks_i(chicken.getEggLayTime())};
        }
        return new Timespan[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Timespan[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Timespan timespan = delta instanceof Timespan[] ? ((Timespan[]) delta)[0] : null;
        if (timespan == null) return;
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof Chicken chicken) {
            chicken.setEggLayTime((int) timespan.getTicks_i());
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
        return "the chicken egg lay time of " + (e == null ? "" : entityExpression.getSingle(e));
    }
}
