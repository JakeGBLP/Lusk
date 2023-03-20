package me.jake.lusk.elements.experimental.block;

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
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

@Name("Displayed Block")
@Description("""
        """)
@Examples({""})
@Since("1.0.2")
    public class ExprDisplayBlock extends SimpleExpression<BlockData> {
    static {
        Skript.registerExpression(ExprDisplayBlock.class, BlockData.class, ExpressionType.COMBINED,
                "[the] [display] block of %entity%");
    }
    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected BlockData @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity != null) {
            if (entity.getType() == EntityType.BLOCK_DISPLAY) {
                return new BlockData[]{((BlockDisplay) entity).getBlock()};
            }
        }
        return new BlockData[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends BlockData> getReturnType() {
        return BlockData.class;
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
        if (!(entity.getType() == EntityType.BLOCK_DISPLAY))
            return;
        BlockDisplay display = (BlockDisplay) entity;
        BlockData blockData;
        if (delta[0] instanceof BlockData blockData1) {
            blockData = blockData1;
        } else if (delta[0] instanceof Block block) {
            blockData = block.getBlockData();
        } else if (delta[0] instanceof ItemType itemType) {
            blockData =  Bukkit.createBlockData(itemType.getMaterial());
        } else if (delta[0] instanceof Slot slot) {
            blockData =  Bukkit.createBlockData(Objects.requireNonNull(slot.getItem()).getType());
        } else {
            return;
        }
        display.setBlock(blockData);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        if (e == null) return "";
        return "the display item of " + entityExpression.getSingle(e);
    }
}