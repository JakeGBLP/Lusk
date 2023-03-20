package me.jake.lusk.elements.experimental.text;

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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Display Text Opacity")
@Description("""
        """)
@Examples({""})
@Since("1.0.2")
    public class ExprDisplayTextOpacity extends SimpleExpression<Byte> {
    static {
        Skript.registerExpression(ExprDisplayTextOpacity.class, Byte.class, ExpressionType.COMBINED,
                "[the] [display] text opacity of %entity%");
    }
    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected Byte @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity != null) {
            if (entity.getType() == EntityType.TEXT_DISPLAY) {
                return new Byte[]{((TextDisplay) entity).getTextOpacity()};
            }
        }
        return new Byte[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Byte> getReturnType() {
        return Byte.class;
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case RESET, SET, ADD, REMOVE, DELETE -> CollectionUtils.array(Byte[].class);
            default -> new Class[0];
        };
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Entity entity = entityExpression.getSingle(e);
        if (entity == null) return;
        if (!(entity.getType() == EntityType.TEXT_DISPLAY))
            return;
        TextDisplay display = (TextDisplay) entity;
        byte value = display.getTextOpacity();
        switch (mode) {
            case RESET, DELETE:
                value = 0;
                break;
            case SET:
                value = (byte) delta[0];
                break;
            case REMOVE:
                value = (byte) (value - (byte) delta[0]);
                break;
            case ADD:
                value = (byte) (value + (byte) delta[0]);
                break;
            default:
                return;
        }
        display.setTextOpacity(value);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        if (e == null) return "";
        return "the display text opacity of " + entityExpression.getSingle(e);
    }
}