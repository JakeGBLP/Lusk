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
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Allay - Duplication Cooldown")
@Description("Returns the duplication cooldown of an Allay.\nCan be set.")
@Examples({"broadcast duplication cooldown of target"})
@Since("1.0.2")
public class ExprAllayDuplicationCooldown extends SimpleExpression<Timespan> {
    static {
        Skript.registerExpression(ExprAllayDuplicationCooldown.class, Timespan.class, ExpressionType.COMBINED,
                "[the] allay duplication cool[ ]down of %entity%");

    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected Timespan @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof Allay allay) {
            return new Timespan[]{Timespan.fromTicks(allay.getDuplicationCooldown())};
        }
        return new Timespan[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) ? new Class[]{Timespan.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (entityExpression.getSingle(e) instanceof Allay allay) {
            if (mode == Changer.ChangeMode.SET && delta[0] instanceof Timespan timespan) {
                allay.setDuplicationCooldown(timespan.getTicks());
            } else allay.resetDuplicationCooldown();
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
        return "the allay duplication cooldown of " + (e == null ? "" : entityExpression.toString(e,debug));
    }
}