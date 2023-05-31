package me.jake.lusk.elements.expressions;

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
import ch.njol.util.coll.CollectionUtils;
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
                "[the] [allay] duplication cooldown of %entity%");

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
            return new Timespan[]{Timespan.fromTicks_i(allay.getDuplicationCooldown())};
        }
        return new Timespan[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Timespan[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Entity entity = entityExpression.getSingle(e);
        if (!(entity instanceof Allay allay)) return;
        if (mode == Changer.ChangeMode.SET) {
            Timespan timespan = delta instanceof Timespan[] ? ((Timespan[]) delta)[0] : null;
            if (timespan == null) return;
            allay.setDuplicationCooldown(timespan.getTicks_i());
        } else {
            allay.resetDuplicationCooldown();
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
        return "the allay duplication cooldown of " + (e == null ? "" : entityExpression.getSingle(e));
    }
}