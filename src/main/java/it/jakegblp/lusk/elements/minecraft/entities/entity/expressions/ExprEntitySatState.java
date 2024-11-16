package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Sittable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Animal - Sitting State")
@Description("Returns whether an entity is sat. Can be set.\n(Cats, Wolves, Parrots, Pandas and Foxes)")
@Examples({"broadcast sitting state of target"})
@Since("1.0.2")
@DocumentationId("9057")
@SuppressWarnings("unused")
public class ExprEntitySatState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprEntitySatState.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] s(at|it[ting]) [down] state of %entity%",
                "%entity%'[s] s(at|it[ting]) [down] state",
                "whether %entity% is s(at|it[ting]) [down] [or not]",
                "whether [or not] %entity% is s(at|it[ting]) [down]");
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
        if (livingEntity instanceof Sittable sittable) {
            bool = sittable.isSitting();
        } else {
            bool = livingEntity.getPose() == Pose.SITTING;
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
            if (livingEntityExpression.getSingle(e) instanceof Sittable sittable) sittable.setSitting(aBoolean);
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
        return "the sitting state of " + (e == null ? "" : livingEntityExpression.toString(e, debug));
    }
}