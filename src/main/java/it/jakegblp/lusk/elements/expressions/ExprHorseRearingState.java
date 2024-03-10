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
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Horse - Rearing State")
@Description("Returns whether or not an horse is rearing.\nCan be set.")
@Examples({"broadcast rearing state of target"})
@Since("1.0.3")
public class ExprHorseRearingState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprHorseRearingState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] horse [is] rearing state of %entity%",
                "%entity%'[s] horse [is] rearing state",
                "whether [the] horse %entity% is rearing [or not]",
                "whether [or not] [the] horse %entity% is rearing");
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
        if (entity instanceof AbstractHorse horse) {
            return new Boolean[]{horse.isRearing()};
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
            if (entityExpression.getSingle(e) instanceof AbstractHorse horse)
                horse.setRearing(aBoolean);
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
        return "the horse is rearing state of " + (e == null ? "" : entityExpression.toString(e,debug));
    }
}