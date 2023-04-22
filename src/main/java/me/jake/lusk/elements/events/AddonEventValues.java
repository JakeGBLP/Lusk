package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.slot.EquipmentSlot;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class AddonEventValues {

    public AddonEventValues() {
    }

    static {
        if (Skript.classExists("org.bukkit.event.block.BlockDispenseArmorEvent")) {
            EventValues.registerEventValue(BlockDispenseArmorEvent.class, Entity.class, new Getter<Entity, BlockDispenseArmorEvent>() {
                @Override
                public @NotNull Entity get(final BlockDispenseArmorEvent e) {
                    return e.getTargetEntity();
                }
            }, 0);
        } if (Skript.classExists("org.bukkit.event.inventory.PrepareGrindstoneEvent")) {
            EventValues.registerEventValue(org.bukkit.event.inventory.PrepareGrindstoneEvent.class, Block.class, new Getter<Block, org.bukkit.event.inventory.PrepareGrindstoneEvent>() {
                @Override
                public @NotNull Block get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return Objects.requireNonNull(e.getInventory().getLocation()).getBlock();
                }
            }, 0);
            EventValues.registerEventValue(org.bukkit.event.inventory.PrepareGrindstoneEvent.class, Inventory.class, new Getter<Inventory, org.bukkit.event.inventory.PrepareGrindstoneEvent>() {
                @Override
                public @NotNull Inventory get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return Objects.requireNonNull(e.getInventory());
                }
            }, 0);
            EventValues.registerEventValue(org.bukkit.event.inventory.PrepareGrindstoneEvent.class, Location.class, new Getter<Location, org.bukkit.event.inventory.PrepareGrindstoneEvent>() {
                @Override
                public @NotNull Location get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return Objects.requireNonNull(e.getInventory().getLocation());
                }
            }, 0);
            EventValues.registerEventValue(org.bukkit.event.inventory.PrepareGrindstoneEvent.class, Slot.class, new Getter<Slot, org.bukkit.event.inventory.PrepareGrindstoneEvent>() {
                @Override
                public @NotNull Slot get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    Slot slot = new InventorySlot(e.getInventory(), 2);
                    ItemStack result = e.getResult();
                    slot.setItem(result);
                    if (result != null) {
                        slot.setAmount(result.getAmount());
                    }
                    return slot;
                }
            }, 0);
        } else if (Skript.classExists("com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent")) {
            EventValues.registerEventValue(com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class, Block.class, new Getter<Block, com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent>() {
                @Override
                public @NotNull Block get(final com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent e) {
                    Location location = e.getInventory().getLocation();
                    assert location != null;
                    return location.getBlock();
                }
            }, 0);
            EventValues.registerEventValue(com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class, Inventory.class, new Getter<Inventory, com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent>() {
                @Override
                public @NotNull Inventory get(final com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory();
                }
            }, 0);
            EventValues.registerEventValue(com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class, Location.class, new Getter<Location, com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent>() {
                @Override
                public @NotNull Location get(final com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent e) {
                    Location location = e.getInventory().getLocation();
                    assert location != null;
                    return location;
                }
            }, 0);
            EventValues.registerEventValue(com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class, Slot.class, new Getter<Slot, com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent>() {
                @Override
                public @NotNull Slot get(final com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent e) {
                    Slot slot = new InventorySlot(e.getInventory(), 2);
                    ItemStack result = e.getResult();
                    slot.setItem(result);
                    if (result != null) {
                        slot.setAmount(result.getAmount());
                    }
                    return slot;
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PlayerBedFailEnterEvent")) {
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Block.class, new Getter<Block, PlayerBedFailEnterEvent>() {
                @Override
                public @NotNull Block get(final PlayerBedFailEnterEvent e) {
                    return e.getBed();
                }
            }, 0);
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Location.class, new Getter<Location, PlayerBedFailEnterEvent>() {
                @Override
                public @NotNull Location get(final PlayerBedFailEnterEvent e) {
                    return e.getBed().getLocation();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.entity.EntityInsideBlockEvent")) {
            EventValues.registerEventValue(EntityInsideBlockEvent.class, Block.class, new Getter<Block, EntityInsideBlockEvent>() {
                @Override
                public @NotNull Block get(final EntityInsideBlockEvent e) {
                    return e.getBlock();
                }
            }, 0);
        } if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent")) {
            EventValues.registerEventValue(PlayerStartSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStartSpectatingEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerStartSpectatingEntityEvent e) {
                    return e.getCurrentSpectatorTarget();
                }
            }, -1);
            EventValues.registerEventValue(PlayerStartSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStartSpectatingEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerStartSpectatingEntityEvent e) {
                    return e.getNewSpectatorTarget();
                }
            }, 0);
            EventValues.registerEventValue(PlayerStartSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStartSpectatingEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerStartSpectatingEntityEvent e) {
                    return e.getNewSpectatorTarget();
                }
            }, 1);
        } if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent")) {
            EventValues.registerEventValue(PlayerStopSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStopSpectatingEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerStopSpectatingEntityEvent e) {
                    return e.getSpectatorTarget();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent")) {
             EventValues.registerEventValue(PlayerFlowerPotManipulateEvent.class, ItemStack.class, new Getter<ItemStack, PlayerFlowerPotManipulateEvent>() {
                 @Override
                 public @NotNull ItemStack get(final PlayerFlowerPotManipulateEvent e) {
                     return e.getItem();
                 }
             }, 0);
             EventValues.registerEventValue(PlayerFlowerPotManipulateEvent.class, Block.class, new Getter<Block, PlayerFlowerPotManipulateEvent>() {
                 @Override
                 public @NotNull Block get(final PlayerFlowerPotManipulateEvent e) {
                     return e.getFlowerpot();
                 }
             }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PlayerItemFrameChangeEvent")) {
            EventValues.registerEventValue(PlayerItemFrameChangeEvent.class, Entity.class, new Getter<Entity, PlayerItemFrameChangeEvent>() {
                @Override
                public @NotNull Entity get(final PlayerItemFrameChangeEvent e) {
                    return e.getItemFrame();
                }
            }, 0);
            EventValues.registerEventValue(PlayerItemFrameChangeEvent.class, ItemStack.class, new Getter<ItemStack, PlayerItemFrameChangeEvent>() {
                @Override
                public @NotNull ItemStack get(final PlayerItemFrameChangeEvent e) {
                    return e.getItemStack();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PlayerLoomPatternSelectEvent")) {
            EventValues.registerEventValue(PlayerLoomPatternSelectEvent.class, PatternType.class, new Getter<PatternType, PlayerLoomPatternSelectEvent>() {
                @Override
                public @NotNull PatternType get(final PlayerLoomPatternSelectEvent e) {
                    return e.getPatternType();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PlayerNameEntityEvent")) {
            EventValues.registerEventValue(PlayerNameEntityEvent.class, Entity.class, new Getter<Entity, PlayerNameEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerNameEntityEvent e) {
                    return e.getEntity();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PlayerStopUsingItemEvent")) {
            EventValues.registerEventValue(PlayerStopUsingItemEvent.class, ItemStack.class, new Getter<ItemStack, PlayerStopUsingItemEvent>() {
                @Override
                public @NotNull ItemStack get(final PlayerStopUsingItemEvent e) {
                    return e.getItem();
                }
            }, 0);
            EventValues.registerEventValue(PlayerStopUsingItemEvent.class, Timespan.class, new Getter<Timespan, PlayerStopUsingItemEvent>() {
                @Override
                public @NotNull Timespan get(final PlayerStopUsingItemEvent e) {
                    return Timespan.fromTicks_i(e.getTicksHeldFor());
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PrePlayerAttackEntityEvent")) {
            EventValues.registerEventValue(PrePlayerAttackEntityEvent.class, Entity.class, new Getter<Entity, PrePlayerAttackEntityEvent>() {
                @Override
                public @NotNull Entity get(final PrePlayerAttackEntityEvent e) {
                    return e.getAttacked();
                }
            }, 0);
        } if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerPostRespawnEvent")) {
            EventValues.registerEventValue(PlayerPostRespawnEvent.class, Location.class, new Getter<Location, PlayerPostRespawnEvent>() {
                @Override
                public @NotNull Location get(final PlayerPostRespawnEvent e) {
                    return e.getRespawnedLocation();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PlayerInventorySlotChangeEvent")) {
            EventValues.registerEventValue(PlayerInventorySlotChangeEvent.class, ItemStack.class, new Getter<ItemStack, PlayerInventorySlotChangeEvent>() {
                @Override
                public @NotNull ItemStack get(PlayerInventorySlotChangeEvent event) {
                    return event.getNewItemStack();
                }
            }, 0);
            EventValues.registerEventValue(PlayerInventorySlotChangeEvent.class, ItemStack.class, new Getter<ItemStack, PlayerInventorySlotChangeEvent>() {
                @Override
                public @NotNull ItemStack get(PlayerInventorySlotChangeEvent event) {
                    return event.getOldItemStack();
                }
            }, -1);
            EventValues.registerEventValue(PlayerInventorySlotChangeEvent.class, Slot.class, new Getter<Slot, PlayerInventorySlotChangeEvent>() {
                @Override
                public @NotNull Slot get(PlayerInventorySlotChangeEvent event) {
                    PlayerInventory inv = event.getPlayer().getInventory();
                    int slotIndex = event.getSlot();
                    if (slotIndex >= 36) {
                        return new EquipmentSlot(event.getPlayer(), slotIndex);
                    } else {
                        return new InventorySlot(inv, slotIndex);
                    }
                }
            }, 0);
        } if (Skript.classExists("org.bukkit.event.inventory.FurnaceExtractEvent")) {
            EventValues.registerEventValue(FurnaceExtractEvent.class, Slot.class, new Getter<Slot, FurnaceExtractEvent>() {
                @Override
                public @NotNull Slot get(final FurnaceExtractEvent e) {
                    InventoryView inventoryView = e.getPlayer().getOpenInventory();
                    ItemStack cursor = inventoryView.getCursor();
                    Slot slot = new InventorySlot(inventoryView.getTopInventory(), 2);
                    slot.setItem(cursor);
                    if (cursor != null) {
                        slot.setAmount(cursor.getAmount());
                    }
                    return slot;
                }
            }, 0);
            EventValues.registerEventValue(FurnaceExtractEvent.class, Inventory.class, new Getter<Inventory, FurnaceExtractEvent>() {
                @Override
                public @NotNull Inventory get(final FurnaceExtractEvent e) {
                    return e.getPlayer().getOpenInventory().getTopInventory();
                }
            }, 0);
            EventValues.registerEventValue(FurnaceExtractEvent.class, ItemType.class, new Getter<ItemType, FurnaceExtractEvent>() {
                @Override
                public @NotNull ItemType get(final FurnaceExtractEvent e) {
                    return new ItemType(e.getItemType());
                }
            }, 0);
            EventValues.registerEventValue(FurnaceExtractEvent.class, Integer.class, new Getter<Integer, FurnaceExtractEvent>() {
                @Override
                public @NotNull Integer get(final FurnaceExtractEvent e) {
                    return e.getItemAmount();
                }
            }, 0);
        } if (Skript.classExists("org.bukkit.event.inventory.FurnaceStartSmeltEvent")) {
            EventValues.registerEventValue(FurnaceStartSmeltEvent.class, Integer.class, new Getter<Integer, FurnaceStartSmeltEvent>() {
                @Override
                public @NotNull Integer get(final FurnaceStartSmeltEvent e) {
                    return e.getTotalCookTime();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderEvent")) {
            EventValues.registerEventValue(WorldBorderEvent.class, World.class, new Getter<World, WorldBorderEvent>() {
                @Override
                public @NotNull World get(final WorldBorderEvent e) {
                    return e.getWorld();
                }
            }, 0);
        } if (Skript.classExists("com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent")) {
            EventValues.registerEventValue(ProfileWhitelistVerifyEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, ProfileWhitelistVerifyEvent>() {
                @Override
                public @NotNull OfflinePlayer get(final ProfileWhitelistVerifyEvent e) {
                    UUID id = e.getPlayerProfile().getId();
                    assert id != null;
                    return Bukkit.getOfflinePlayer(id);
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.block.BlockPreDispenseEvent")) {
            EventValues.registerEventValue(BlockPreDispenseEvent.class, Slot.class, new Getter<Slot, BlockPreDispenseEvent>() {
                @Override
                public @NotNull Slot get(final BlockPreDispenseEvent e) {
                    Inventory inventory = ((InventoryHolder)e.getBlock()).getInventory();
                    Slot slot = new InventorySlot(inventory, e.getSlot());
                    slot.setItem(e.getItemStack());
                    return slot;
                }
            }, 0);
        } if (Skript.classExists("org.bukkit.event.block.BrewingStartEvent")) {
            EventValues.registerEventValue(BrewingStartEvent.class, Inventory.class, new Getter<Inventory, BrewingStartEvent>() {
                @Override
                public @NotNull Inventory get(final BrewingStartEvent e) {
                    return ((InventoryHolder) e.getBlock()).getInventory();
                }
            }, 0);
            EventValues.registerEventValue(BrewingStartEvent.class, ItemType.class, new Getter<ItemType, BrewingStartEvent>() {
                @Override
                public @NotNull ItemType get(final BrewingStartEvent e) {
                    ItemStack itemStack = ((BrewingStand) e.getBlock()).getInventory().getIngredient();
                    assert itemStack != null;
                    return new ItemType(itemStack);
                }
            }, 0);
        } if (Skript.classExists("org.bukkit.event.inventory.BrewEvent")) {
            EventValues.registerEventValue(BrewEvent.class, Inventory.class, new Getter<Inventory, BrewEvent>() {
                @Override
                public @NotNull Inventory get(final BrewEvent e) {
                    return e.getContents();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.entity.ElderGuardianAppearanceEvent")) {
            EventValues.registerEventValue(ElderGuardianAppearanceEvent.class, Player.class, new Getter<Player, ElderGuardianAppearanceEvent>() {
                @Override
                public @NotNull Player get(final ElderGuardianAppearanceEvent e) {
                    return e.getAffectedPlayer();
                }
            }, 0);
        } if (Skript.classExists(" org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            EventValues.registerEventValue(EntityEnterLoveModeEvent.class, CommandSender.class, new Getter<CommandSender, EntityEnterLoveModeEvent>() {
                @Override
                public @NotNull CommandSender get(final EntityEnterLoveModeEvent e) {
                    HumanEntity human = e.getHumanEntity();
                    assert human != null;
                    return human;
                }
            }, 0);
        } if (Skript.classExists(" org.bukkit.event.entity.EntityPoseChangeEvent")) {
            EventValues.registerEventValue(EntityPoseChangeEvent.class, Pose.class, new Getter<Pose, EntityPoseChangeEvent>() {
                @Override
                public @NotNull Pose get(final EntityPoseChangeEvent e) {
                    return e.getPose();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent")) {
            EventValues.registerEventValue(EntityPushedByEntityAttackEvent.class, Vector.class, new Getter<Vector, EntityPushedByEntityAttackEvent>() {
                @Override
                public @NotNull Vector get(final EntityPushedByEntityAttackEvent e) {
                    return e.getAcceleration();
                }
            }, 0);
        } if (Skript.classExists("org.bukkit.event.block.BlockDamageAbortEvent")) {
            EventValues.registerEventValue(BlockDamageAbortEvent.class, Player.class, new Getter<Player, BlockDamageAbortEvent>() {
                @Override
                public @NotNull Player get(final BlockDamageAbortEvent e) {
                    return e.getPlayer();
                }
            }, 0);
        } if (Skript.classExists("org.bukkit.event.block.BellRingEvent")) {
            EventValues.registerEventValue(org.bukkit.event.block.BellRingEvent.class, Entity.class, new Getter<Entity, org.bukkit.event.block.BellRingEvent>() {
                @Override
                public @NotNull Entity get(final org.bukkit.event.block.BellRingEvent e) {
                    Entity entity = e.getEntity();
                    assert entity != null;
                    return entity;
                }
            }, 0);
        } else if (Skript.classExists("io.papermc.paper.event.block.BellRingEvent")) {
            EventValues.registerEventValue(BellRingEvent.class, Entity.class, new Getter<Entity, BellRingEvent>() {
                @Override
                public @NotNull Entity get(final BellRingEvent e) {
                    Entity entity = e.getEntity();
                    assert entity != null;
                    return entity;
                }
            }, 0);
        } if (Skript.classExists("com.destroystokyo.paper.event.block.BeaconEffectEvent")) {
            EventValues.registerEventValue(BeaconEffectEvent.class, Player.class, new Getter<Player, BeaconEffectEvent>() {
                @Override
                public @NotNull Player get(final BeaconEffectEvent e) {
                    return e.getPlayer();
                }
            }, 0);
        } if (Skript.classExists("io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent")) {
            EventValues.registerEventValue(PlayerChangeBeaconEffectEvent.class, Block.class, new Getter<Block, PlayerChangeBeaconEffectEvent>() {
                @Override
                public @NotNull Block get(final PlayerChangeBeaconEffectEvent e) {
                    Block block = e.getBeacon();
                    assert block != null;
                    return block;
                }
            }, 0);
        } if (Skript.classExists("org.bukkit.event.entity.PlayerLeashEntityEvent")) {
            EventValues.registerEventValue(PlayerLeashEntityEvent.class, Player.class, new Getter<Player, PlayerLeashEntityEvent>() {
                @Override
                public @NotNull Player get(final PlayerLeashEntityEvent e) {
                    return e.getPlayer();
                }
            }, 0);
        }
    }
}