package it.jakegblp.lusk.utils;

import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
    public static boolean isFullBlock(Block block) {
        List<BoundingBox> boxes = new ArrayList<>(block.getCollisionShape().getBoundingBoxes());
        if (boxes.size() != 1) return false;
        BoundingBox box = boxes.getFirst();
        return box.getWidthX() == 1 && box.getHeight() == 1 && box.getWidthZ() == 1;
    }
}
