package it.jakegblp.lusk.skript.utils;

import ch.njol.skript.aliases.ItemType;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.function.Consumer;

@NullMarked
public abstract class BlockStateHolder<T> {

    protected final T holder;

    protected BlockStateHolder(T holder) {
        this.holder = holder;
    }

    public static Block block(org.bukkit.block.Block block) {
        return new Block(block);
    }

    public static Item item(ItemType item) {
        return new Item(item);
    }

    public abstract Optional<BlockState> getBlockState();

    @SuppressWarnings("unchecked")
    public <B extends BlockState> Optional<B> getBlockState(Class<B> blockStateClass) {
        var optionalBlockState = getBlockState();
        if (optionalBlockState.isPresent()) {
            var blockState = optionalBlockState.get();
            return blockStateClass.isInstance(blockState) ? Optional.of((B) blockState) : Optional.empty();
        } else  {
            return Optional.empty();
        }
    }

    public abstract <B extends BlockState> boolean editBlockState(Class<B> blockStateClass, Consumer<B> consumer);

    public static class Block extends BlockStateHolder<org.bukkit.block.Block> {

        protected Block(org.bukkit.block.Block holder) {
            super(holder);
        }

        @Override
        public Optional<BlockState> getBlockState() {
            return Optional.of(holder.getState());
        }

        @Override
        @SuppressWarnings("unchecked")
        public <B extends BlockState> boolean editBlockState(Class<B> blockStateClass, Consumer<B> consumer) {
            var optionalBlockState = getBlockState();
            if (optionalBlockState.isPresent()) {
                var blockState = optionalBlockState.get();
                if (blockStateClass.isInstance(blockState)) {
                    consumer.accept((B) blockState);
                    return blockState.update();
                }
            }
            return false;
        }
    }

    public static class Item extends BlockStateHolder<ItemType> {

        protected Item(ItemType holder) {
            super(holder);
        }

        @Override
        public Optional<BlockState> getBlockState() {
            return holder.getItemMeta() instanceof BlockStateMeta blockStateMeta ? Optional.of(blockStateMeta.getBlockState()) : Optional.empty();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <B extends BlockState> boolean editBlockState(Class<B> blockStateClass, Consumer<B> consumer) {
            var optionalBlockState = getBlockState();
            if (optionalBlockState.isPresent()) {
                var blockState = optionalBlockState.get();
                if (blockStateClass.isInstance(blockState)) {
                    consumer.accept((B) blockState);
                    var meta = (BlockStateMeta) holder.getItemMeta();
                    meta.setBlockState(blockState);
                    holder.setItemMeta(meta);
                    return true;
                }
            }
            return false;
        }
    }
}