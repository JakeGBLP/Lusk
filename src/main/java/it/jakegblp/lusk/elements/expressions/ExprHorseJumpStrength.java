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
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Horse - Jump Strength")
@Description("Returns the jump strength of an horse.\nCan be set.")
@Examples({"broadcast jumping strength of target"})
@Since("1.0.3")
public class ExprHorseJumpStrength extends SimpleExpression<Double> {
    static {
        Skript.registerExpression(ExprHorseJumpStrength.class, Double.class, ExpressionType.COMBINED,
                "[the] horse jump[ing] (strength|force) of %entity%",
                "%entity%'[s] horse jump[ing] (strength|force)");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected Double @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof AbstractHorse horse) {
            return new Double[]{horse.getJumpStrength()};
        }
        return new Double[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Double.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Double aDouble)
            if (entityExpression.getSingle(e) instanceof AbstractHorse horse)
                horse.setJumpStrength(aDouble);
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
        return "the horse jumping strength of " + (e == null ? "" : entityExpression.toString(e,debug));
    }
}