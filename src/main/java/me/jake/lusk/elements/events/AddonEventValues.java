package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.slot.EquipmentSlot;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class AddonEventValues {

    public AddonEventValues() {
    }

    static {

        // 1.0.0

        // Dispense Armor Event
        EventValues.registerEventValue(BlockDispenseArmorEvent.class, Entity.class, new Getter<Entity, BlockDispenseArmorEvent>() {
            @Override
            public @NotNull Entity get(final BlockDispenseArmorEvent e) {
                return e.getTargetEntity();
            }
        }, 0);
        EventValues.registerEventValue(PrepareGrindstoneEvent.class, Block.class, new Getter<Block, PrepareGrindstoneEvent>() {
            @Override
            public @NotNull Block get(final PrepareGrindstoneEvent e) {
                return Objects.requireNonNull(e.getInventory().getLocation()).getBlock();
            }
        }, 0);
        EventValues.registerEventValue(PrepareGrindstoneEvent.class, Inventory.class, new Getter<Inventory, PrepareGrindstoneEvent>() {
            @Override
            public @NotNull Inventory get(final PrepareGrindstoneEvent e) {
                return Objects.requireNonNull(e.getInventory());
            }
        }, 0);
        EventValues.registerEventValue(PrepareGrindstoneEvent.class, Location.class, new Getter<Location, PrepareGrindstoneEvent>() {
            @Override
            public @NotNull Location get(final PrepareGrindstoneEvent e) {
                return Objects.requireNonNull(e.getInventory().getLocation());
            }
        }, 0);
        EventValues.registerEventValue(PrepareGrindstoneEvent.class, Slot.class, new Getter<Slot, PrepareGrindstoneEvent>() {
            @Override
            public @NotNull Slot get(final PrepareGrindstoneEvent e) {
                Slot slot = new InventorySlot(e.getInventory(), 2);
                slot.setItem(e.getResult());
                slot.setAmount(Objects.requireNonNull(e.getResult()).getAmount());
                return slot;
            }
        }, 0);

        // Paper

        if (Skript.classExists("io.papermc.paper.event.player.PlayerBedFailEnterEvent")) {
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Block.class, new Getter<Block, PlayerBedFailEnterEvent>() {
                @Override
                public @NotNull Block get(final PlayerBedFailEnterEvent e) {
                    return Objects.requireNonNull(e.getBed());
                }
            }, 0);
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Location.class, new Getter<Location, PlayerBedFailEnterEvent>() {
                @Override
                public @NotNull Location get(final PlayerBedFailEnterEvent e) {
                    return Objects.requireNonNull(e.getBed().getLocation());
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.entity.EntityInsideBlockEvent")) {
            EventValues.registerEventValue(EntityInsideBlockEvent.class, Block.class, new Getter<Block, EntityInsideBlockEvent>() {
                @Override
                public @NotNull Block get(final EntityInsideBlockEvent e) {
                    return Objects.requireNonNull(e.getBlock());
                }
            }, 0);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent")) {
            EventValues.registerEventValue(PlayerStartSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStartSpectatingEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerStartSpectatingEntityEvent e) {
                    return Objects.requireNonNull(e.getCurrentSpectatorTarget());
                }
            }, -1);
            EventValues.registerEventValue(PlayerStartSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStartSpectatingEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerStartSpectatingEntityEvent e) {
                    return Objects.requireNonNull(e.getNewSpectatorTarget());
                }
            }, 0);
            EventValues.registerEventValue(PlayerStartSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStartSpectatingEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerStartSpectatingEntityEvent e) {
                    return Objects.requireNonNull(e.getNewSpectatorTarget());
                }
            }, 1);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent")) {
            EventValues.registerEventValue(PlayerStopSpectatingEntityEvent.class, Entity.class, new Getter<Entity, PlayerStopSpectatingEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerStopSpectatingEntityEvent e) {
                    return Objects.requireNonNull(e.getSpectatorTarget());
                }
            }, 0);
        }
         if (Skript.classExists("io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent")) {
             EventValues.registerEventValue(PlayerFlowerPotManipulateEvent.class, ItemStack.class, new Getter<ItemStack, PlayerFlowerPotManipulateEvent>() {
                 @Override
                 public @NotNull ItemStack get(final PlayerFlowerPotManipulateEvent e) {
                     return Objects.requireNonNull(e.getItem());
                 }
             }, 0);
             EventValues.registerEventValue(PlayerFlowerPotManipulateEvent.class, Block.class, new Getter<Block, PlayerFlowerPotManipulateEvent>() {
                 @Override
                 public @NotNull Block get(final PlayerFlowerPotManipulateEvent e) {
                     return Objects.requireNonNull(e.getFlowerpot());
                 }
             }, 0);
         }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerItemFrameChangeEvent")) {
            EventValues.registerEventValue(PlayerItemFrameChangeEvent.class, Entity.class, new Getter<Entity, PlayerItemFrameChangeEvent>() {
                @Override
                public @NotNull Entity get(final PlayerItemFrameChangeEvent e) {
                    return Objects.requireNonNull(e.getItemFrame().getVehicle());
                }
            }, 0);
            EventValues.registerEventValue(PlayerItemFrameChangeEvent.class, ItemStack.class, new Getter<ItemStack, PlayerItemFrameChangeEvent>() {
                @Override
                public @NotNull ItemStack get(final PlayerItemFrameChangeEvent e) {
                    return Objects.requireNonNull(e.getItemStack());
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerLoomPatternSelectEvent")) {
            EventValues.registerEventValue(PlayerLoomPatternSelectEvent.class, PatternType.class, new Getter<PatternType, PlayerLoomPatternSelectEvent>() {
                @Override
                public @NotNull PatternType get(final PlayerLoomPatternSelectEvent e) {
                    return Objects.requireNonNull(e.getPatternType());
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerNameEntityEvent")) {
            EventValues.registerEventValue(PlayerNameEntityEvent.class, Entity.class, new Getter<Entity, PlayerNameEntityEvent>() {
                @Override
                public @NotNull Entity get(final PlayerNameEntityEvent e) {
                    return Objects.requireNonNull(e.getEntity());
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerStopUsingItemEvent")) {
            EventValues.registerEventValue(PlayerStopUsingItemEvent.class, ItemStack.class, new Getter<ItemStack, PlayerStopUsingItemEvent>() {
                @Override
                public @NotNull ItemStack get(final PlayerStopUsingItemEvent e) {
                    return Objects.requireNonNull(e.getItem());
                }
            }, 0);
            EventValues.registerEventValue(PlayerStopUsingItemEvent.class, Timespan.class, new Getter<Timespan, PlayerStopUsingItemEvent>() {
                @Override
                public @NotNull Timespan get(final PlayerStopUsingItemEvent e) {
                    return Timespan.fromTicks_i(e.getTicksHeldFor());
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PrePlayerAttackEntityEvent")) {
            EventValues.registerEventValue(PrePlayerAttackEntityEvent.class, Entity.class, new Getter<Entity, PrePlayerAttackEntityEvent>() {
                @Override
                public @NotNull Entity get(final PrePlayerAttackEntityEvent e) {
                    return Objects.requireNonNull(e.getAttacked());
                }
            }, 0);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerPostRespawnEvent")) {
            EventValues.registerEventValue(PlayerPostRespawnEvent.class, Location.class, new Getter<Location, PlayerPostRespawnEvent>() {
                @Override
                public @NotNull Location get(final PlayerPostRespawnEvent e) {
                    return Objects.requireNonNull(e.getRespawnedLocation());
                }
            }, 0);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerInventorySlotChangeEvent")) {
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
        }

        // 1.0.1

        // Furnace Extract Event
        EventValues.registerEventValue(FurnaceExtractEvent.class, Slot.class, new Getter<Slot, FurnaceExtractEvent>() {
            @Override
            public @NotNull Slot get(final FurnaceExtractEvent e) {
                Slot slot = new InventorySlot(Objects.requireNonNull(e.getPlayer().getOpenInventory().getTopInventory()), 2);
                slot.setItem(e.getPlayer().getOpenInventory().getCursor());
                slot.setAmount(Objects.requireNonNull(e.getPlayer().getOpenInventory().getCursor()).getAmount());
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

        // Furnace Start Smelting
        EventValues.registerEventValue(FurnaceStartSmeltEvent.class, Integer.class, new Getter<Integer, FurnaceStartSmeltEvent>() {
            @Override
            public @NotNull Integer get(final FurnaceStartSmeltEvent e) {
                return e.getTotalCookTime();
            }
        }, 0);

        // 1.0.2

        // WorldBorder Events
        if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderEvent")) {
            EventValues.registerEventValue(WorldBorderEvent.class, World.class, new Getter<World, WorldBorderEvent>() {
                @Override
                public @NotNull World get(final WorldBorderEvent e) {
                    return Objects.requireNonNull(e.getWorld());
                }
            }, 0);
        }
        // Whitelist Events
        if (Skript.classExists("com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent")) {
            EventValues.registerEventValue(ProfileWhitelistVerifyEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, ProfileWhitelistVerifyEvent>() {
                @Override
                public @NotNull OfflinePlayer get(final ProfileWhitelistVerifyEvent e) {
                    return Bukkit.getOfflinePlayer(Objects.requireNonNull(e.getPlayerProfile().getId()));
                }
            }, 0);
        }
        // Dispense Events
        if (Skript.classExists("io.papermc.paper.event.block.BlockPreDispenseEvent")) {
            EventValues.registerEventValue(BlockPreDispenseEvent.class, Slot.class, new Getter<Slot, BlockPreDispenseEvent>() {
                @Override
                public @NotNull Slot get(final BlockPreDispenseEvent e) {
                    Inventory inventory = ((InventoryHolder)e.getBlock()).getInventory();
                    Slot slot = new InventorySlot(inventory, e.getSlot());
                    slot.setItem(e.getItemStack());
                    return slot;
                }
            }, 0);
        }
        // Brewing Events
        EventValues.registerEventValue(BrewingStartEvent.class, Inventory.class, new Getter<Inventory, BrewingStartEvent>() {
            @Override
            public @NotNull Inventory get(final BrewingStartEvent e) {
                return ((InventoryHolder)e.getBlock()).getInventory();
            }
        }, 0);
        EventValues.registerEventValue(BrewingStartEvent.class, ItemType.class, new Getter<ItemType, BrewingStartEvent>() {
            @Override
            public @NotNull ItemType get(final BrewingStartEvent e) {
                BrewerInventory inventory = ((BrewingStand)e.getBlock()).getInventory();
                return new ItemType(Objects.requireNonNull(inventory.getIngredient()));
            }
        }, 0);
        EventValues.registerEventValue(BrewEvent.class, Inventory.class, new Getter<Inventory, BrewEvent>() {
            @Override
            public @NotNull Inventory get(final BrewEvent e) {
                return e.getContents();
            }
        }, 0);
        // Elder Guardian Events
        EventValues.registerEventValue(ElderGuardianAppearanceEvent.class, Player.class, new Getter<Player, ElderGuardianAppearanceEvent>() {
            @Override
            public @NotNull Player get(final ElderGuardianAppearanceEvent e) {
                return e.getAffectedPlayer();
            }
        }, 0);
        // Love Mode Event
        EventValues.registerEventValue(EntityEnterLoveModeEvent.class, Player.class, new Getter<Player, EntityEnterLoveModeEvent>() {
            @Override
            public @NotNull Player get(final EntityEnterLoveModeEvent e) {
                return (Player) Objects.requireNonNull(e.getHumanEntity());
            }
        }, 0);
        // Pose Event
        EventValues.registerEventValue(EntityPoseChangeEvent.class, Pose.class, new Getter<Pose, EntityPoseChangeEvent>() {
            @Override
            public @NotNull Pose get(final EntityPoseChangeEvent e) {
                return e.getPose();
            }
        }, 0);
        // Damage Push
        if (Skript.classExists("io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent")) {
            EventValues.registerEventValue(EntityPushedByEntityAttackEvent.class, Vector.class, new Getter<Vector, EntityPushedByEntityAttackEvent>() {
                @Override
                public @NotNull Vector get(final EntityPushedByEntityAttackEvent e) {
                    return e.getAcceleration();
                }
            }, 0);
        }
    }
}