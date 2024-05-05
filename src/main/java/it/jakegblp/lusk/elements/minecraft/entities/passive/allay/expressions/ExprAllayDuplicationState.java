package it.jakegblp.lusk.elements.minecraft.entities.passive.allay.expressions;

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
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Allay - Duplication State")
@Description("Returns whether or not the allay can duplicate itself.\nCan be set.")
@Examples({"broadcast duplication state of target"})
@Since("1.0.2")
public class ExprAllayDuplicationState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprAllayDuplicationState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] allay can be duplicated state of %entity%",
                "%entity%'[s] allay can be duplicated state",
                "whether [the] allay %entity% can be duplicated [or not]",
                "whether [or not] [the] allay %entity% can be duplicated");

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
        if (entity instanceof Allay allay) return new Boolean[]{allay.canDuplicate()};
        return new Boolean[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean)
            if (entityExpression.getSingle(e) instanceof Allay allay)
                allay.setCanDuplicate(aBoolean);
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
        return "the allay can be duplicated state of " + (e == null ? "" : entityExpression.toString(e, debug));
    }
}