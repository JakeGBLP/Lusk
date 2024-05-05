package it.jakegblp.lusk.elements.minecraft.entities.hostile.enderman.expressions;

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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Enderman - Held Block")
@Description("Returns the Held Block of an Enderman.")
@Examples({"broadcast held block of target"})
@Since("1.0.2")
public class ExprEndermanHeldBlock extends SimpleExpression<BlockData> {
    static {
        Skript.registerExpression(ExprEndermanHeldBlock.class, BlockData.class, ExpressionType.COMBINED,
                "[the] [enderman] (held|carried) block[[ |-]data] of %livingentity%",
                "%livingentity%'[s] [enderman] (held|carried) block[[ |-]data]");

    }

    private Expression<LivingEntity> livingEntityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }

    @Override
    protected BlockData @NotNull [] get(@NotNull Event e) {
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        if (livingEntity instanceof Enderman enderman) {
            return new BlockData[]{enderman.getCarriedBlock()};
        }
        return new BlockData[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case SET, RESET, DELETE ->
                    new Class[]{Block.class, BlockData.class, Slot.class, ItemStack.class, ItemType.class};
            default -> null;
        };
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        if (livingEntity instanceof Enderman enderman) {
            if (mode == Changer.ChangeMode.SET) {
                Object o = delta[0];
                if (o instanceof BlockData blockData1) {
                    enderman.setCarriedBlock(blockData1);
                } else if (o instanceof Block block) {
                    enderman.setCarriedBlock(block.getBlockData());
                } else if (o instanceof ItemType itemType) {
                    if (itemType.getMaterial().isBlock()) {
                        enderman.setCarriedBlock(Bukkit.createBlockData(itemType.getMaterial()));
                    }
                } else if (o instanceof ItemStack itemStack) {
                    Material material = itemStack.getType();
                    if (material.isBlock()) {
                        enderman.setCarriedBlock(Bukkit.createBlockData(material));
                    }
                } else if (o instanceof Slot slot) {
                    ItemStack itemStack = slot.getItem();
                    if (itemStack != null) {
                        Material material = itemStack.getType();
                        if (material.isBlock()) {
                            enderman.setCarriedBlock(Bukkit.createBlockData(material));
                        }
                    }
                }
            } else {
                enderman.setCarriedBlock(null);
            }
        }
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
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the enderman held block-data of " + (e == null ? "" : livingEntityExpression.toString(e, debug));
    }
}