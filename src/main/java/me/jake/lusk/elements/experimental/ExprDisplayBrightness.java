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
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

@Name("Display Brightness")
@Description("""
        """)
@Examples({""})
@Since("1.0.2")
    public class ExprDisplayBrightness extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprDisplayBrightness.class, Integer.class, ExpressionType.COMBINED,
                "[the] [display] block (light|brightness) of %entity%",
                "[the] [display] sky (light|brightness) of %entity%");
    }
    private boolean sky;
    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        sky = matchedPattern == 1;
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity != null) {
            if (entity.getType() == EntityType.TEXT_DISPLAY || entity.getType() == EntityType.BLOCK_DISPLAY || entity.getType() == EntityType.ITEM_DISPLAY) {
                return new Integer[]{(sky ? ((Display) entity).getBrightness().getSkyLight() : ((Display) entity).getBrightness().getBlockLight())};
            }
        }
        return new Integer[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case RESET, SET, ADD, REMOVE, DELETE -> CollectionUtils.array(Integer[].class);
            default -> new Class[0];
        };
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Entity entity = entityExpression.getSingle(e);
        if (entity == null) return;
        if (!(entity.getType() == EntityType.TEXT_DISPLAY || entity.getType() == EntityType.BLOCK_DISPLAY || entity.getType() == EntityType.ITEM_DISPLAY))
            return;
        Display display = (Display) entity;
        int value = sky ? display.getBrightness().getSkyLight() : display.getBrightness().getBlockLight();
        switch (mode) {
            case RESET, DELETE:
                value = 0;
                break;
            case SET:
                value = (int) delta[0];
                break;
            case REMOVE:
                value = value - (int) delta[0];
                break;
            case ADD:
                value = value + (int) delta[0];
                break;
            default:
                return;
        }
        if (sky) {
            display.setBrightness(new Display.Brightness(value,display.getBrightness().getBlockLight()));
        } else {
            display.setBrightness(new Display.Brightness(display.getBrightness().getSkyLight(), value));
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        if (e == null) return "";
        return "the display brightness " + entityExpression.getSingle(e);
    }
}