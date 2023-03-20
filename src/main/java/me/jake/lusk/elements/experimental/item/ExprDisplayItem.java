package me.jake.lusk.elements.experimental.item;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.slot.Slot;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

@Name("Displayed Item")
@Description("""
        """)
@Examples({""})
@Since("1.0.2")
    public class ExprDisplayItem extends SimpleExpression<ItemType> {
    static {
        Skript.registerExpression(ExprDisplayItem.class, ItemType.class, ExpressionType.COMBINED,
                "[the] [display] item of %entity%");
    }
    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected ItemType @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity != null) {
            if (entity.getType() == EntityType.ITEM_DISPLAY) {
                return new ItemType[]{new ItemType(Objects.requireNonNull(((ItemDisplay) entity).getItemStack()))};
            }
        }
        return new ItemType[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(ItemType[].class);
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
        ItemStack item;
        if (delta[0] instanceof ItemType itemType) {
            item = itemType.getRandom();
        } else if (delta[0] instanceof Slot slot) {
            item = slot.getItem();
        } else if (delta[0] instanceof Block block) {
            item = new ItemStack(block.getType());
        } else if (delta[0] instanceof BlockData blockData) {
            item = new ItemStack(blockData.getMaterial());
        } else {
            return;
        }
        display.setItemStack(item);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        if (e == null) return "";
        return "the display item of " + entityExpression.getSingle(e);
    }
}