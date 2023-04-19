package me.jake.lusk.elements.expressions;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Skeleton Horse - Trap Time")
@Description("Returns the trap time of the skeleton horse.")
@Examples({"on damage of skeleton horse:\n\tbroadcast trap time of victim"})
@Since("1.0.3")
public class ExprSkeletonHorseTrapTime extends SimpleExpression<Timespan> {
    static {
        Skript.registerExpression(ExprSkeletonHorseTrapTime.class, Timespan.class, ExpressionType.COMBINED,
                "[the] [skeleton horse] trap time of %livingentity%");
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
        if (entity instanceof SkeletonHorse skeletonHorse) {
            return new Timespan[]{Timespan.fromTicks_i(skeletonHorse.getTrapTime())};
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
        if (entity instanceof SkeletonHorse skeletonHorse) {
            skeletonHorse.setTrapTime((int) timespan.getTicks_i());
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
        return "the skeleton horse trap time of " + (e == null ? "" : entityExpression.getSingle(e));
    }
}
