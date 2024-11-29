package it.jakegblp.lusk.api.wrappers;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

public class BlockWrapper {
    private final Block block;
    private final BlockData blockData;

    public BlockWrapper(Block block) {
        this.block = block;
        this.blockData = null;
    }

    public BlockWrapper(BlockState blockState) {
        this.block = blockState.getBlock();
        this.blockData = null;
    }

    public BlockWrapper(Object object) {
        if (object instanceof Block aBlock) {
            this.block = aBlock;
            this.blockData = null;
        } else if (object instanceof BlockState aBlockState) {
            this.block = aBlockState.getBlock();
            this.blockData = null;
        } else if (object instanceof BlockData aBlockData) {
            this.block = null;
            this.blockData = aBlockData;
        } else {
            this.block = null;
            this.blockData = null;
        }
    }

    public Block getBlock() {
        return block;
    }

    public boolean hasOnlyBlockData() {
        return block == null && blockData != null;
    }

    public BlockData getBlockData() {
        return hasOnlyBlockData() ? blockData : block.getBlockData();
    }

    public void setBlockData(BlockData blockData) {
        if (block != null) {
            block.setBlockData(blockData);
        }
    }

    /**
     * Sets whether this block is waterlogged.
     * @param waterLog whether to make this block waterlogged or not
     */
    public void setWaterLogged(boolean waterLog) {
        if (getBlockData() instanceof Waterlogged waterlogged) {
            waterlogged.setWaterlogged(waterLog);
            setBlockData(waterlogged);
        }
    }

    /**
     * @return whether this block is waterlogged, false if it can't be waterlogged.
     */
    public boolean isWaterLogged() {
        return getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged();
    }

    /**
     * @return whether this block can be waterlogged.
     */
    public boolean canBeWaterlogged() {
        return getBlockData() instanceof Waterlogged;
    }
}
