package it.jakegblp.lusk.utils;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * @param object either a {@link Block block}, {@link BlockData blockdata} or {@link BlockState blockstate}
     * @return whether the provided object is waterlogged, false if it can't be waterlogged.
     * @see BlockUtils#getAsWaterlogged(Object)
     */
    public static boolean isWaterLogged(Object object) {
        if (object instanceof Waterlogged waterlogged) {
            return waterlogged.isWaterlogged();
        } else if (object instanceof Block block && block.getBlockData() instanceof Waterlogged waterlogged) {
            return waterlogged.isWaterlogged();
        } else if (object instanceof BlockState blockState && blockState.getBlockData() instanceof Waterlogged waterlogged) {
            return waterlogged.isWaterlogged();
        }
        return false;
    }

    /**
     * @param object either a {@link Block block}, {@link BlockData blockdata} or {@link BlockState blockstate}
     * @return the provided object as {@link Waterlogged an object that can waterlogged}.
     */
    public static Waterlogged getAsWaterlogged(Object object) {
        BlockData blockData;
        if (object instanceof BlockData data) {
            blockData = data;
        } else if (object instanceof Block block) {
            blockData = block.getBlockData();
        } else if (object instanceof BlockState blockState) {
            blockData = blockState.getBlockData();
        } else return null;
        if (blockData instanceof Waterlogged waterlogged) {
            return waterlogged;
        }
        return null;
    }
    /**
     * @param object either a {@link Block block}, {@link BlockData blockdata} or {@link BlockState blockstate}
     * @return whether the provided object can be waterlogged.
     * @see BlockUtils#getAsWaterlogged(Object) 
     */
    public static boolean canBeWaterlogged(Object object) {
        return getAsWaterlogged(object) != null;
    }


    /**
     * Sets whether the provided object is waterlogged.
     *
     * @param object either a {@link Block block}, {@link BlockData blockdata} or {@link BlockState blockstate}
     * @see BlockUtils#getAsWaterlogged(Object)
     */
    public static void setWaterlogged(Object object, boolean waterlog) {
        Waterlogged waterlogged = getAsWaterlogged(object);
        if (waterlogged != null) {
            waterlogged.setWaterlogged(waterlog);
        }
    }
}
