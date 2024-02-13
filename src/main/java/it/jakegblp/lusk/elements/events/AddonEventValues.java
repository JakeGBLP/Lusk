package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import io.papermc.paper.event.block.BellRingEvent;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import io.papermc.paper.event.entity.ElderGuardianAppearanceEvent;
import io.papermc.paper.event.entity.EntityInsideBlockEvent;
import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import io.papermc.paper.event.player.*;
import io.papermc.paper.event.world.border.WorldBorderEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public final class AddonEventValues {

    static {
        if (Skript.classExists("org.bukkit.event.block.BlockDispenseArmorEvent")) {
            EventValues.registerEventValue(BlockDispenseArmorEvent.class, Entity.class, new Getter<>() {
                @Override
                public Entity get(final BlockDispenseArmorEvent e) {
                    return e.getTargetEntity();
                }
            }, 0);
        }
        if (Skript.classExists("org.bukkit.event.inventory.PrepareGrindstoneEvent")) {
            EventValues.registerEventValue(org.bukkit.event.inventory.PrepareGrindstoneEvent.class, Block.class, new Getter<>() {
                @Override
                public Block get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    Location location = e.getInventory().getLocation();
                    if (location != null) {
                        return location.getBlock();
                    } else {
                        return null;
                    }
                }
            }, 0);
            EventValues.registerEventValue(org.bukkit.event.inventory.PrepareGrindstoneEvent.class, Inventory.class, new Getter<>() {
                @Override
                public Inventory get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory();
                }
            }, 0);
            EventValues.registerEventValue(org.bukkit.event.inventory.PrepareGrindstoneEvent.class, Location.class, new Getter<>() {
                @Override
                public Location get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory().getLocation();
                }
            }, 0);
            EventValues.registerEventValue(org.bukkit.event.inventory.PrepareGrindstoneEvent.class, Slot.class, new Getter<>() {
                @Override
                public Slot get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return new InventorySlot(e.getInventory(), 2);
                }
            }, 0);
        } else if (Skript.classExists("com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent")) {
            EventValues.registerEventValue(com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class, Block.class, new Getter<>() {
                @Override
                public @Nullable Block get(final com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent e) {
                    Location location = e.getInventory().getLocation();
                    if (location != null) {
                        return location.getBlock();
                    } else {
                        return null;
                    }
                }
            }, 0);
            EventValues.registerEventValue(com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @NotNull Inventory get(final com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory();
                }
            }, 0);
            EventValues.registerEventValue(com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class, Location.class, new Getter<>() {
                @Override
                public @Nullable Location get(final com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory().getLocation();
                }
            }, 0);
            EventValues.registerEventValue(com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class, Slot.class, new Getter<>() {
                @Override
                public @NotNull Slot get(final com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent e) {
                    return new InventorySlot(e.getInventory(), 2);
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerBedFailEnterEvent")) {
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(final PlayerBedFailEnterEvent e) {
                    return e.getBed();
                }
            }, 0);
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Location.class, new Getter<>() {
                @Override
                public @NotNull Location get(final PlayerBedFailEnterEvent e) {
                    return e.getBed().getLocation();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.entity.EntityInsideBlockEvent")) {
            EventValues.registerEventValue(EntityInsideBlockEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(final EntityInsideBlockEvent e) {
                    return e.getBlock();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent")) {
            EventValues.registerEventValue(PlayerFlowerPotManipulateEvent.class, ItemStack.class, new Getter<>() {
                @Override
                public @NotNull ItemStack get(final PlayerFlowerPotManipulateEvent e) {
                    return e.getItem();
                }
            }, 0);
            EventValues.registerEventValue(PlayerFlowerPotManipulateEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(final PlayerFlowerPotManipulateEvent e) {
                    return e.getFlowerpot();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerItemFrameChangeEvent")) {
            EventValues.registerEventValue(PlayerItemFrameChangeEvent.class, Entity.class, new Getter<>() {
                @Override
                public @NotNull Entity get(final PlayerItemFrameChangeEvent e) {
                    return e.getItemFrame();
                }
            }, 0);
            EventValues.registerEventValue(PlayerItemFrameChangeEvent.class, ItemStack.class, new Getter<>() {
                @Override
                public @NotNull ItemStack get(final PlayerItemFrameChangeEvent e) {
                    return e.getItemStack();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerLoomPatternSelectEvent")) {
            EventValues.registerEventValue(PlayerLoomPatternSelectEvent.class, PatternType.class, new Getter<>() {
                @Override
                public @NotNull PatternType get(final PlayerLoomPatternSelectEvent e) {
                    return e.getPatternType();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerNameEntityEvent")) {
            EventValues.registerEventValue(PlayerNameEntityEvent.class, Entity.class, new Getter<>() {
                @Override
                public @NotNull Entity get(final PlayerNameEntityEvent e) {
                    return e.getEntity();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PrePlayerAttackEntityEvent")) {
            EventValues.registerEventValue(PrePlayerAttackEntityEvent.class, Entity.class, new Getter<>() {
                @Override
                public @NotNull Entity get(final PrePlayerAttackEntityEvent e) {
                    return e.getAttacked();
                }
            }, 0);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerPostRespawnEvent")) {
            EventValues.registerEventValue(PlayerPostRespawnEvent.class, Location.class, new Getter<>() {
                @Override
                public @NotNull Location get(final PlayerPostRespawnEvent e) {
                    return e.getRespawnedLocation();
                }
            }, 0);
        }
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceExtractEvent")) {
            EventValues.registerEventValue(FurnaceExtractEvent.class, Slot.class, new Getter<>() {
                @Override
                public @NotNull Slot get(final FurnaceExtractEvent e) {
                    InventoryView inventoryView = e.getPlayer().getOpenInventory();
                    return new InventorySlot(inventoryView.getTopInventory(), 2);
                }
            }, 0);
            EventValues.registerEventValue(FurnaceExtractEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @NotNull Inventory get(final FurnaceExtractEvent e) {
                    return e.getPlayer().getOpenInventory().getTopInventory();
                }
            }, 0);
            EventValues.registerEventValue(FurnaceExtractEvent.class, ItemType.class, new Getter<>() {
                @Override
                public @NotNull ItemType get(final FurnaceExtractEvent e) {
                    return new ItemType(e.getItemType());
                }
            }, 0);
            EventValues.registerEventValue(FurnaceExtractEvent.class, Integer.class, new Getter<>() {
                @Override
                public @NotNull Integer get(final FurnaceExtractEvent e) {
                    return e.getItemAmount();
                }
            }, 0);
        }
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceStartSmeltEvent")) {
            EventValues.registerEventValue(FurnaceStartSmeltEvent.class, Integer.class, new Getter<>() {
                @Override
                public @NotNull Integer get(final FurnaceStartSmeltEvent e) {
                    return e.getTotalCookTime();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderEvent")) {
            EventValues.registerEventValue(WorldBorderEvent.class, World.class, new Getter<>() {
                @Override
                public @NotNull World get(final WorldBorderEvent e) {
                    return e.getWorld();
                }
            }, 0);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent")) {
            EventValues.registerEventValue(ProfileWhitelistVerifyEvent.class, OfflinePlayer.class, new Getter<>() {
                @Override
                public @Nullable OfflinePlayer get(final ProfileWhitelistVerifyEvent e) {
                    UUID id = e.getPlayerProfile().getId();
                    if (id != null) {
                        return Bukkit.getOfflinePlayer(id);
                    }
                    return null;
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockPreDispenseEvent")) {
            EventValues.registerEventValue(BlockPreDispenseEvent.class, Slot.class, new Getter<>() {
                @Override
                public @Nullable Slot get(final BlockPreDispenseEvent e) {
                    Block block = e.getBlock();
                    if (block instanceof BlockInventoryHolder inventoryHolder) {
                        Inventory inventory = inventoryHolder.getInventory();
                        return new InventorySlot(inventory, e.getSlot());
                    }
                    return null;
                }
            }, 0);
        }
        if (Skript.classExists("org.bukkit.event.block.BrewingStartEvent")) {
            EventValues.registerEventValue(BrewingStartEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @NotNull Inventory get(final BrewingStartEvent e) {
                    return ((InventoryHolder) e.getBlock()).getInventory();
                }
            }, 0);
            EventValues.registerEventValue(BrewingStartEvent.class, ItemType.class, new Getter<>() {
                @Override
                public @Nullable ItemType get(final BrewingStartEvent e) {
                    ItemStack itemStack = ((BrewingStand) e.getBlock()).getInventory().getIngredient();
                    if (itemStack != null) {
                        return new ItemType(itemStack);
                    }
                    return null;
                }
            }, 0);
        }
        if (Skript.classExists("org.bukkit.event.inventory.BrewEvent")) {
            EventValues.registerEventValue(BrewEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @NotNull Inventory get(final BrewEvent e) {
                    return e.getContents();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.entity.ElderGuardianAppearanceEvent")) {
            EventValues.registerEventValue(ElderGuardianAppearanceEvent.class, Player.class, new Getter<>() {
                @Override
                public @NotNull Player get(final ElderGuardianAppearanceEvent e) {
                    return e.getAffectedPlayer();
                }
            }, 0);
        }
        if (Skript.classExists(" org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            EventValues.registerEventValue(EntityEnterLoveModeEvent.class, CommandSender.class, new Getter<>() {
                @Override
                public @Nullable CommandSender get(final EntityEnterLoveModeEvent e) {
                    return e.getHumanEntity();
                }
            }, 0);
        }
        if (Skript.classExists(" org.bukkit.event.entity.EntityPoseChangeEvent")) {
            EventValues.registerEventValue(EntityPoseChangeEvent.class, Pose.class, new Getter<>() {
                @Override
                public @NotNull Pose get(final EntityPoseChangeEvent e) {
                    return e.getPose();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent")) {
            EventValues.registerEventValue(EntityPushedByEntityAttackEvent.class, Vector.class, new Getter<>() {
                @Override
                public @NotNull Vector get(final EntityPushedByEntityAttackEvent e) {
                    return e.getAcceleration();
                }
            }, 0);
        }
        if (Skript.classExists("org.bukkit.event.block.BellRingEvent")) {
            EventValues.registerEventValue(org.bukkit.event.block.BellRingEvent.class, Entity.class, new Getter<>() {
                @Override
                public @Nullable Entity get(final org.bukkit.event.block.BellRingEvent e) {
                    return e.getEntity();
                }
            }, 0);
        } else if (Skript.classExists("io.papermc.paper.event.block.BellRingEvent")) {
            EventValues.registerEventValue(BellRingEvent.class, Entity.class, new Getter<>() {
                @Override
                public @Nullable Entity get(final BellRingEvent e) {
                    return e.getEntity();
                }
            }, 0);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.block.BeaconEffectEvent")) {
            EventValues.registerEventValue(BeaconEffectEvent.class, Player.class, new Getter<>() {
                @Override
                public @NotNull Player get(final BeaconEffectEvent e) {
                    return e.getPlayer();
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent")) {
            EventValues.registerEventValue(PlayerChangeBeaconEffectEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(final PlayerChangeBeaconEffectEvent e) {
                    return e.getBeacon();
                }
            }, 0);
        }
        if (Skript.classExists("org.bukkit.event.block.FluidLevelChangeEvent")) {
            EventValues.registerEventValue(FluidLevelChangeEvent.class, BlockData.class, new Getter<>() {
                @Override
                public @NotNull BlockData get(final FluidLevelChangeEvent e) {
                    return e.getNewData();
                }
            }, 0);
        }
        if (Skript.classExists("org.bukkit.event.inventory.HopperInventorySearchEvent")) {
            EventValues.registerEventValue(HopperInventorySearchEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @Nullable Inventory get(final HopperInventorySearchEvent e) {
                    return e.getInventory();
                }
            }, 0);
        }
    }

    public AddonEventValues() {
    }
}