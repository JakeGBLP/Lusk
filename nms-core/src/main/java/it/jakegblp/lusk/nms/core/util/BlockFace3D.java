package it.jakegblp.lusk.nms.core.util;

import org.bukkit.block.BlockFace;

public class BlockFace3D {

    public static BlockFace faceFrom3D(int id) {
        return switch (id) {
            case 0 -> BlockFace.DOWN;
            case 1 -> BlockFace.UP;
            case 2 -> BlockFace.NORTH;
            case 3 -> BlockFace.SOUTH;
            case 4 -> BlockFace.WEST;
            case 5 -> BlockFace.EAST;
            default -> BlockFace.SELF;
        };
    }

    public static int faceTo3D(BlockFace face) {
        return switch (face) {
            case DOWN -> 0;
            case UP -> 1;
            case NORTH -> 2;
            case SOUTH -> 3;
            case WEST -> 4;
            case EAST -> 5;
            default -> 1;
        };
    }

}
