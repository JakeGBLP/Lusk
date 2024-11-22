package it.jakegblp.lusk.api.wrappers;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.Nullable;

public class BlockDataWrapper {
    private final Block block;

    public BlockDataWrapper(Block block) {
        this.block = block;
    }

    public BlockDataWrapper(BlockState blockState) {
        this.block = blockState.getBlock();
    }

    @Nullable
    public static BlockDataWrapper create(Object object) {
        if (object instanceof Block) {
            return new BlockDataWrapper((Block) object);
        } else if (object instanceof BlockState) {
            return new BlockDataWrapper((BlockState) object);
        }
        return null;
    }

    public Block getBlock() {
        return block;
    }

    public BlockData getBlockData() {
        return block.getBlockData();
    }

    public void setBlockData(BlockData blockData) {
        block.setBlockData(blockData);
    }

    /**
     * Sets whether this block is waterlogged.
     * @param waterLog whether to make this block waterlogged or not
     */
    public boolean setWaterLogged(boolean waterLog) {
        if (getBlockData() instanceof Waterlogged waterlogged) {
            waterlogged.setWaterlogged(waterLog);
            setBlockData(waterlogged);
            return true;
        }
        return false;
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
