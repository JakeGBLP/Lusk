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
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Enderman - Has Been Stared At State")
@Description("Returns whether or not an enderman has been stared at.")
@Examples({"broadcast has been stared at state of target"})
@Since("1.0.2")
public class ExprEndermanBeenStaredAt extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprEndermanBeenStaredAt.class, Boolean.class, ExpressionType.COMBINED,
                "[the] enderman [has] been stared at state of %entity%",
                "%entity%'[s] enderman [has] been stared at state",
                "whether [the] enderman %entity% has been stared at [or not]",
                "whether [or not] [the] enderman %entity% has been stared at");

    }

    private Expression<LivingEntity> livingEntityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        boolean bool;
        if (livingEntity instanceof Enderman enderman) {
            bool = enderman.hasBeenStaredAt();
        } else {
            bool = false;
        }
        return new Boolean[]{bool};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean)
            if (livingEntityExpression.getSingle(e) instanceof Enderman enderman) {
                enderman.setHasBeenStaredAt(aBoolean);
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
        return "the enderman has been stared at state of " + (e == null ? "" : livingEntityExpression.toString(e,debug));
    }
}