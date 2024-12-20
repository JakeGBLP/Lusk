package it.jakegblp.lusk.elements.minecraft.entities.dolphin.expressions;

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
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Dolphin - Has Fish State")
@Description("Returns whether or not a dolphin has a fish.\nCan be set.")
@Examples({"broadcast has fish state of target"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprDolphinHasFishState extends SimpleExpression<Boolean> {
    static {
        // todo: simple property expression, verbose util method, util, plural
        Skript.registerExpression(ExprDolphinHasFishState.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] dolphin [has] been fed fish state of %entity%",
                "%entity%'[s] dolphin [has] been fed fish state",
                "whether [the] dolphin %entity% has been fed fish [or not]",
                "whether [or not [the] dolphin %entity% has been fed fish]");
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
        if (entity instanceof Dolphin dolphin) {
            return new Boolean[]{dolphin.hasFish()};
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
            if (entityExpression.getSingle(e) instanceof Dolphin dolphin) dolphin.setHasFish(aBoolean);
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
        return "the dolphin has been fed fish state of " + (e == null ? "" : entityExpression.toString(e, debug));
    }
}