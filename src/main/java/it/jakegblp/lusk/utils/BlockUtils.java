package it.jakegblp.lusk.utils;

import ch.njol.skript.aliases.ItemType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static it.jakegblp.lusk.utils.ItemUtils.getNullableItemStack;
import static it.jakegblp.lusk.utils.ItemUtils.getNullableItemType;

public class BlockUtils {
    /**
     * Gets whether this block is exactly 1x1x1
     *
     * @return whether this block is full
     */
    public static boolean isFullBlock(Block block) {
        List<BoundingBox> boxes = new ArrayList<>(block.getCollisionShape().getBoundingBoxes());
        if (boxes.size() != 1) return false;
        BoundingBox box = boxes.getFirst();
        return box.getWidthX() == 1 && box.getHeight() == 1 && box.getWidthZ() == 1;
    }

    @Nullable
    public static ItemType getBrewingIngredient(Block block) {
        BlockState state = block.getState();
        if (state instanceof BrewingStand brewingStand) {
            return getNullableItemType(brewingStand.getInventory().getIngredient());
        }
        return null;
    }

    public static void setBrewingIngredient(Block block, ItemType ingredient) {
        BlockState state = block.getState();
        if (state instanceof BrewingStand brewingStand) {
            brewingStand.getInventory().setIngredient(getNullableItemStack(ingredient));
        }
    }

    @Nullable
    public static ItemType getBrewingFuel(Block block) {
        BlockState state = block.getState();
        if (state instanceof BrewingStand brewingStand) {
            return getNullableItemType(brewingStand.getInventory().getFuel());
        }
        return null;
    }

    public static void setBrewingFuel(Block block, ItemType ingredient) {
        BlockState state = block.getState();
        if (state instanceof BrewingStand brewingStand) {
            brewingStand.getInventory().setFuel(getNullableItemStack(ingredient));
        }
    }
}
