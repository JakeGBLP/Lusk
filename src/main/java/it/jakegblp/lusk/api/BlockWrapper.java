package it.jakegblp.lusk.api;

import ch.njol.skript.aliases.ItemType;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_1;
import static it.jakegblp.lusk.utils.ItemUtils.getNullableItemStack;
import static it.jakegblp.lusk.utils.ItemUtils.getNullableItemType;

public class BlockWrapper {

    @Nullable
    private final Block block;
    @Nullable
    private final ItemType item;
    @Nullable
    private final BlockStateMeta blockStateMeta;
    @Nullable
    private final BlockState blockState;
    @Nullable
    private final BlockDataMeta blockDataMeta;
    @Nullable
    private final BlockData blockData;
    private final boolean shouldUpdate;

    public BlockWrapper(Object object) {
        this(object,false);
    }

    public BlockWrapper(Object object, boolean shouldUpdate) {
        if (object instanceof Block aBlock) {
            this.block = aBlock;
            this.blockState = null;
            this.item = null;
            this.blockData = null;
            this.blockDataMeta = null;
            this.blockStateMeta = null;
            this.shouldUpdate = false;
        } else if (object instanceof BlockState aBlockState) {
            this.block = null;
            this.blockState = aBlockState;
            this.item = null;
            this.blockData = null;
            this.blockDataMeta = null;
            this.blockStateMeta = null;
            this.shouldUpdate = false;
        } else if (object instanceof ItemType itemType) {
            this.block = null;
            this.blockState = null;
            this.item = itemType;
            this.blockData = null;
            this.blockDataMeta = null;
            this.blockStateMeta = null;
            this.shouldUpdate = shouldUpdate;
        } else if (object instanceof ItemMeta itemMeta) {
            this.block = null;
            this.blockState = null;
            this.item = null;
            this.blockData = null;
            this.blockDataMeta = itemMeta instanceof BlockDataMeta meta ? meta : null;
            this.blockStateMeta = itemMeta instanceof BlockStateMeta meta ? meta : null;
            this.shouldUpdate = false;
        } else if (object instanceof BlockData aBlockData) {
            this.block = null;
            this.blockState = null;
            this.item = null;
            this.blockData = aBlockData;
            this.blockDataMeta = null;
            this.blockStateMeta = null;
            this.shouldUpdate = false;
        } else {
            this.block = null;
            this.blockState = null;
            this.item = null;
            this.blockData = null;
            this.blockDataMeta = null;
            this.blockStateMeta = null;
            this.shouldUpdate = false;
        }
    }

    @Nullable
    public Block getBlock() {
        if (block != null) return block;
        else if (blockState != null) return blockState.getBlock();
        else if (item != null && item.getItemMeta() instanceof BlockStateMeta meta) return meta.getBlockState().getBlock();
        else return null;
    }

    @Nullable
    public BlockData getBlockData() {
        if (block != null) return block.getBlockData();
        else if (blockState != null) return blockState.getBlockData();
        else if (blockStateMeta != null) return blockStateMeta.getBlockState().getBlockData();
        else if (item != null) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta instanceof BlockStateMeta meta)
                return meta.getBlockState().getBlockData();
            else if (itemMeta instanceof BlockDataMeta meta)
                return meta.getBlockData(item.getMaterial());
        }
        return blockData;
    }

    /**
     * Updates the inner Block with the given {@link BlockData},
     * only works if {@link #shouldUpdate()} is true,
     * only valid for {@link Block blocks},
     * {@link ItemType items} whose {@link ItemMeta meta} is an instance of {@link BlockStateMeta} or {@link BlockDataMeta},
     * and {@link ItemMeta item metas} that are instances of the previously mentioned Meta interfaces.
     * @param blockData the new blockdata
     */
    public void updateBlockData(BlockData blockData) {
        if (!shouldUpdate) return;
        if (block != null) block.setBlockData(blockData);
        else if (blockState != null) {
            blockState.setBlockData(blockData);
            blockState.update(true);
        } else if (item != null) {
            if (item.getItemMeta() instanceof BlockStateMeta meta) {
                BlockState blockState = meta.getBlockState();
                blockState.setBlockData(blockData);
                meta.setBlockState(blockState);
                item.setItemMeta(meta);
            } else if (item.getItemMeta() instanceof BlockDataMeta meta) {
                meta.setBlockData(blockData);
                item.setItemMeta(meta);
            }
        } else if (blockStateMeta != null) {
            BlockState blockState = blockStateMeta.getBlockState();
            blockState.setBlockData(blockData);
            blockStateMeta.setBlockState(blockState);
        } else if (blockDataMeta != null) {
            blockDataMeta.setBlockData(blockData);
        }
    }

    /**
     * Updates the inner Block with the given {@link BlockState},
     * only works if {@link #shouldUpdate()} is true,
     * only valid for Items with BlockState.
     * @param blockState the new blockstate
     */
    public void updateBlockState(BlockState blockState) {
        if (!shouldUpdate) return;
        if (item != null) {
            if (item.getItemMeta() instanceof BlockStateMeta meta) {
                meta.setBlockState(blockState);
                item.setItemMeta(meta);
            }
        }
    }

    @Nullable
    public Material getMaterial() {
        if (item != null) return item.getMaterial();
        else if (block != null) return block.getType();
        else if (blockState != null) return blockState.getType();
        else if (blockData != null) return blockData.getMaterial();
        else if (blockStateMeta != null) return blockStateMeta.getBlockState().getType();
        else return null;
    }

    @Nullable
    public BlockState getBlockState() {
        BlockState state = getPlacedBlockState();
        if (state != null) return state;
        else if (item != null && item.getItemMeta() instanceof BlockStateMeta meta) return meta.getBlockState();
        else if (blockStateMeta != null) return blockStateMeta.getBlockState();
        else return null;
    }

    @Nullable
    public Block getPlacedBlock() {
        if (block != null) return block;
        else if (blockState != null && blockState.isPlaced()) return blockState.getBlock();
        return null;
    }

    @Nullable
    public BlockState getPlacedBlockState() {
        if (block != null) return block.getState(!shouldUpdate);
        else if (blockState != null && blockState.isPlaced()) return blockState;
        return null;
    }

    @Nullable
    public BlockStateMeta getBlockStateMeta() {
        if (item != null && item.getItemMeta() instanceof BlockStateMeta meta) return meta;
        else return blockStateMeta;
    }

    @Nullable
    public BlockDataMeta getBlockDataMeta() {
        if (item != null && item.getItemMeta() instanceof BlockDataMeta meta) return meta;
        else return blockDataMeta;
    }

    public boolean isPlaced() {
        if (block != null) return true;
        else if (blockState != null) return blockState.isPlaced();
        return false;
    }

    public boolean shouldUpdate() {
        return shouldUpdate;
    }

    @Nullable
    public VoxelShape getCollisionShape() {
        Block aBlock = getPlacedBlock();
        if (aBlock == null) return null;
        return aBlock.getCollisionShape();
    }

    /**
     * @return whether this placed block is full.
     */
    public boolean isFull() {
        VoxelShape shape = getCollisionShape();
        if (shape == null) return false;
        List<BoundingBox> boundingBoxes = new ArrayList<>(shape.getBoundingBoxes());
        if (boundingBoxes.size() != 1) return false;
        BoundingBox box = boundingBoxes.getFirst();
        return box.getWidthX() == 1 && box.getHeight() == 1 && box.getWidthZ() == 1;
    }

    /**
     * Sets whether this block is waterlogged.
     * @param waterLog whether to make this block waterlogged or not
     */
    public void setWaterLogged(boolean waterLog) {
        if (getBlockData() instanceof Waterlogged waterlogged) {
            waterlogged.setWaterlogged(waterLog);
            updateBlockData(waterlogged);
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

    @Nullable
    public ItemType getBrewingFuel() {
        if (getBlockState() instanceof BrewingStand brewingStand) {
            return getNullableItemType(brewingStand.getInventory().getFuel());
        }
        return null;
    }

    public void setBrewingFuel(ItemType ingredient) {
        if (getBlockState() instanceof BrewingStand brewingStand) {
            brewingStand.getInventory().setFuel(getNullableItemStack(ingredient));
            updateBlockState(brewingStand);
        }
    }

    @Nullable
    public Integer getBrewingFuelLevel() {
        if (getBlockState() instanceof BrewingStand brewingStand) {
            return brewingStand.getFuelLevel();
        }
        return null;
    }

    public void setBrewingFuelLevel(Integer brewingFuelLevel) {
        if (getBlockState() instanceof BrewingStand brewingStand) {
            brewingStand.setFuelLevel(brewingFuelLevel == null ? 0 : brewingFuelLevel);
            updateBlockState(brewingStand);
        }
    }

    @Nullable
    public ItemType getBrewingIngredient() {
        if (getBlockState() instanceof BrewingStand brewingStand) {
            return getNullableItemType(brewingStand.getInventory().getIngredient());
        }
        return null;
    }

    public void setBrewingIngredient(ItemType ingredient) {
        if (getBlockState() instanceof BrewingStand brewingStand) {
            brewingStand.getInventory().setIngredient(getNullableItemStack(ingredient));
            updateBlockState(brewingStand);
        }
    }

    @Nullable
    public Integer getBrewingTime() {
        if (getBlockState() instanceof BrewingStand brewingStand) {
            return brewingStand.getBrewingTime();
        }
        return null;
    }

    public void setBrewingTime(Integer integer) {
        if (getBlockState() instanceof BrewingStand brewingStand) {
            brewingStand.setBrewingTime(integer == null ? 0 : integer);
            updateBlockState(brewingStand);
        }
    }

    @Nullable
    public ItemType getJukeboxRecord() {
        if (getBlockState() instanceof Jukebox jukebox) {
            return getNullableItemType(jukebox.getRecord());
        }
        return null;
    }

    public void setJukeboxRecord(ItemType itemType) {
        if (getBlockState() instanceof Jukebox jukebox) {
            jukebox.setRecord(getNullableItemStack(itemType));
            updateBlockState(jukebox);
        }
    }

    public boolean isJukeboxPlaying() {
        return getBlockState() instanceof Jukebox jukebox && jukebox.isPlaying();
    }

    public void startPlayingJukebox() {
        if (getBlockState() instanceof Jukebox jukebox) {
            jukebox.startPlaying();
        }
    }

    public void stopPlayingJukebox() {
        if (getBlockState() instanceof Jukebox jukebox) {
            jukebox.stopPlaying();
        }
    }

    public void ejectJukeboxDisc() {
        if (getBlockState() instanceof Jukebox jukebox) {
            jukebox.eject();
        }
    }

    /**
     * Requires {@link Constants#MINECRAFT_1_20_1} to be true.
     * @return whether the sign is waxed
     */
    public boolean isSignWaxed() {
        return getBlockState() instanceof Sign sign && sign.isWaxed();
    }

    /**
     * Sets whether the sign is waxed.<br>
     * Requires {@link Constants#MINECRAFT_1_20_1} to be true.
     */
    public void setIsSignWaxed(boolean isSignWaxed) {
        if (getBlockState() instanceof Sign sign) {
            sign.setWaxed(isSignWaxed);
            updateBlockState(sign);
        }
    }

    public boolean isSignEditable() {
        return MINECRAFT_1_20_1 ? !isSignWaxed() : getBlockState() instanceof Sign sign && sign.isEditable();
    }

    public void setIsSignEditable(boolean isSignEditable) {
        if (MINECRAFT_1_20_1) setIsSignWaxed(!isSignEditable);
        else if (getBlockState() instanceof Sign sign) sign.setEditable(isSignEditable);
    }

    @Nullable
    public Integer getBellResonatingTicks() {
        if (getBlockState() instanceof Bell bell)
            return bell.getResonatingTicks();
        return null;
    }

    @Nullable
    public Integer getBellShakingTicks() {
        if (getBlockState() instanceof Bell bell)
            return bell.getShakingTicks();
        return null;
    }

    public boolean isBellResonating() {
        return getBlockState() instanceof Bell bell && bell.isResonating();
    }

    public boolean isBellRinging() {
        return getBlockState() instanceof Bell bell && bell.isShaking();
    }

    public void ringBell(@Nullable Entity entity, @Nullable BlockFace blockFace) {
        if (getBlockState() instanceof Bell bell) {
            if (blockFace != null && blockFace.isCartesian()) {
                if (entity != null) {
                    bell.ring(entity, blockFace);
                } else {
                    bell.ring(blockFace);
                }
            } else if (entity != null) {
                bell.ring(entity);
            } else {
                bell.ring();
            }
        }
    }

    @Nullable
    public Float getSlipperiness() {
        Material material = getMaterial();
        if (material != null && material.isBlock()) return material.getSlipperiness();
        return null;
    }

    @Nullable
    public Float getBlastResistance() {
        Material material = getMaterial();
        if (material != null && material.isBlock()) return material.getBlastResistance();
        return null;
    }
}
