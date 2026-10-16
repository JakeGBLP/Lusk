package it.jakegblp.lusk.skript.utils;

import ch.njol.skript.aliases.ItemType;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jspecify.annotations.NullMarked;

import java.util.function.Consumer;

@NullMarked
public abstract class BlockDataHolder<T> {

    protected final T holder;

    protected BlockDataHolder(T holder) {
        this.holder = holder;
    }

    public static Block block(org.bukkit.block.Block block) {
        return new Block(block);
    }

    public static BlockState blockState(org.bukkit.block.BlockState state) {
        return new BlockState(state);
    }

    public static Item item(ItemType item) {
        return new Item(item);
    }

    public abstract BlockData getBlockData();

    public abstract void setBlockData(BlockData blockData);

    @SuppressWarnings("unchecked")
    public <B extends BlockData> boolean editBlockData(Class<B> blockDataClass, Consumer<B> consumer) {
        var data = getBlockData();
        if (blockDataClass.isInstance(data)) {
            consumer.accept((B) data);
            setBlockData(data);
            return true;
        }
        return false;
    }

    public static class Block extends BlockDataHolder<org.bukkit.block.Block> {

        protected Block(org.bukkit.block.Block holder) {
            super(holder);
        }

        @Override
        public BlockData getBlockData() {
            return holder.getBlockData();
        }

        @Override
        public void setBlockData(BlockData blockData) {
            holder.setBlockData(blockData);
        }
    }

    public static class BlockState extends BlockDataHolder<org.bukkit.block.BlockState> {

        protected BlockState(org.bukkit.block.BlockState holder) {
            super(holder);
        }

        @Override
        public BlockData getBlockData() {
            return holder.getBlockData();
        }

        @Override
        public void setBlockData(BlockData blockData) {
            holder.setBlockData(blockData);
            holder.update(true);
        }
    }

    public static class Item extends BlockDataHolder<ItemType> {

        protected Item(ItemType holder) {
            super(holder);
        }

        @Override
        public BlockData getBlockData() {
            return switch (holder.getItemMeta()) {
                case BlockStateMeta blockStateMeta -> blockStateMeta.getBlockState().getBlockData();
                case BlockDataMeta blockDataMeta -> blockDataMeta.getBlockData(holder.getMaterial());
                default -> throw new IllegalStateException("Invalid item type: " + holder.getMaterial());
            };
        }

        @Override
        public void setBlockData(BlockData blockData) {
            switch (holder.getItemMeta()) {
                case BlockDataMeta blockDataMeta -> {
                    blockDataMeta.setBlockData(blockData);
                    holder.setItemMeta(blockDataMeta);
                }
                case BlockStateMeta blockStateMeta -> {
                    org.bukkit.block.BlockState blockState = blockStateMeta.getBlockState();
                    blockState.setBlockData(blockData);
                    blockStateMeta.setBlockState(blockState);
                    holder.setItemMeta(blockStateMeta);
                }
                default -> throw new IllegalStateException("Invalid item type: " + holder.getMaterial());
            }
        }
    }
}