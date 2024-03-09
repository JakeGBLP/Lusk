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
@Examples({"broadcast skeleton horse trapped state of target"})
@Since("1.0.3")
public class ExprSkeletonHorseTrappedState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprSkeletonHorseTrappedState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] skeleton horse [is] trapped state of %entity%",
                "%entity%'[s] skeleton horse [is] trapped state",
                "whether [the] skeleton horse %entity% is trapped [or not]",
                "whether [or not] [the] skeleton horse %entity% is trapped");
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
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean)
            if (entityExpression.getSingle(e) instanceof SkeletonHorse skeletonHorse)
                skeletonHorse.setTrapped(aBoolean);
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
        return "the skeleton horse is trapped state of " + (e == null ? "" : entityExpression.toString(e,debug));
    }
}