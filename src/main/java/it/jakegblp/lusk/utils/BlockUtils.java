package it.jakegblp.lusk.utils;

import org.bukkit.block.Block;
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
}
