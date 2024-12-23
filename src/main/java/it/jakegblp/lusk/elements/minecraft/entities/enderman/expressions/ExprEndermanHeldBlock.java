package it.jakegblp.lusk.elements.minecraft.entities.enderman.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.util.slot.Slot;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
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

@Name("Enderman - Held Block Data")
@Description("Returns the Held Block Data of the provided Endermen.")
@Examples({"broadcast carried blockdata of target"})
@Since("1.0.2, 1.3 (Plural)")
@SuppressWarnings("unused")
public class ExprEndermanHeldBlock extends SimplerPropertyExpression<LivingEntity,BlockData> {

    static {
        register(ExprEndermanHeldBlock.class,BlockData.class,"[enderman] (held|carried) block[ |-]data","livingentities");
    }

    @Override
    public @Nullable BlockData convert(LivingEntity from) {
        if (from instanceof Enderman enderman) {
            return enderman.getCarriedBlock();
        }
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "enderman carried block data";
    }

    @Override
    public Class<? extends BlockData> getReturnType() {
        return BlockData.class;
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
        for (LivingEntity livingEntity : getExpr().getAll(e)) {
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
    }
}