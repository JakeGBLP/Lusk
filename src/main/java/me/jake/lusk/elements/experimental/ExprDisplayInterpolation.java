package me.jake.lusk.elements.experimental;

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
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Display Interpolation")
@Description("""
        """)
@Examples({""})
@Since("1.0.2")
public class ExprDisplayInterpolation extends SimpleExpression<Timespan> {
    static {
        Skript.registerExpression(ExprDisplayInterpolation.class, Timespan.class, ExpressionType.COMBINED,
                "[the] [display] (interpolation|movement|animation) delay of %entity%",
                "[the] [display] (interpolation|movement|animation) duration of %entity%");
    }
    private Expression<Entity> entityExpression;
    private boolean duration;
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        duration = matchedPattern == 1;
        return true;
    }

    @Override
    protected Timespan @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity != null) {
            if (entity.getType() == EntityType.TEXT_DISPLAY || entity.getType() == EntityType.BLOCK_DISPLAY || entity.getType() == EntityType.ITEM_DISPLAY) {
                return new Timespan[]{new Timespan((duration ? ((Display) entity).getInterpolationDuration() : ((Display) entity).getInterpolationDelay()) * 20L / 1000L)};
            }
        }
        return new Timespan[0];
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
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case RESET, SET, DELETE -> CollectionUtils.array(Timespan[].class);
            default -> new Class[0];
        };
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Entity entity = entityExpression.getSingle(e);
        if (entity == null) return;
        if (!(entity.getType() == EntityType.TEXT_DISPLAY))
            return;
        Display display = (Display) entity;
        long value;
        switch (mode) {
            case RESET, DELETE:
                value = 0;
                break;
            case SET:
                value = ((Timespan) delta[0]).getTicks_i();
                break;
            default:
                return;
        }
        if (duration) {
            display.setInterpolationDuration((int) value);
        } else {
            display.setInterpolationDelay((int) value);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        if (e == null) return "";
        return "the interpolation of " + entityExpression.getSingle(e);
    }
}