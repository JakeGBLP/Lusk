package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Skeleton Horse - Trapped State")
@Description("Returns whether or not a skeleton horse is trapped.\nCan be set.")
@Examples({"broadcast trapped state of target"})
@Since("1.0.3")
public class ExprSkeletonHorseTrappedState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprSkeletonHorseTrappedState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] [skeleton horse] trapped state of %entity%");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof SkeletonHorse skeletonHorse) {
            return new Boolean[]{skeletonHorse.isTrapped()};
        }
        return new Boolean[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Boolean aBoolean = delta instanceof Boolean[] ? ((Boolean[]) delta)[0] : null;
        if (aBoolean == null) return;
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof SkeletonHorse skeletonHorse) {
            skeletonHorse.setTrapped(aBoolean);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the skeleton horse trapped state of " + (e == null ? "" : entityExpression.getSingle(e));
    }
}