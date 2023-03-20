package me.jake.lusk.elements.experimental.item;

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
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Display Item Transform")
@Description("""
        """)
@Examples({""})
@Since("1.0.2")
    public class ExprDisplayItemTransform extends SimpleExpression<ItemDisplay.ItemDisplayTransform> {
    static {
        Skript.registerExpression(ExprDisplayItemTransform.class, ItemDisplay.ItemDisplayTransform.class, ExpressionType.COMBINED,
                "[the] [display] item transform of %entity%");
    }
    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected ItemDisplay.ItemDisplayTransform @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity != null) {
            if (entity.getType() == EntityType.ITEM_DISPLAY) {
                return new ItemDisplay.ItemDisplayTransform[]{((ItemDisplay) entity).getItemDisplayTransform()};
            }
        }
        return new ItemDisplay.ItemDisplayTransform[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemDisplay.ItemDisplayTransform> getReturnType() {
        return ItemDisplay.ItemDisplayTransform.class;
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(ItemDisplay.ItemDisplayTransform[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Entity entity = entityExpression.getSingle(e);
        if (entity == null) return;
        if (!(entity.getType() == EntityType.ITEM_DISPLAY))
            return;
        ItemDisplay display = (ItemDisplay) entity;
        display.setItemDisplayTransform((ItemDisplay.ItemDisplayTransform) delta[0]);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        if (e == null) return "";
        return "the display item transform of " + entityExpression.getSingle(e);
    }
}