package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import io.papermc.paper.event.block.CompostItemEvent;
import io.papermc.paper.event.entity.ElderGuardianAppearanceEvent;
import io.papermc.paper.event.entity.EntityCompostItemEvent;
import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import io.papermc.paper.event.entity.WardenAngerChangeEvent;
import io.papermc.paper.event.player.PlayerBedFailEnterEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

@SuppressWarnings("unused")
public class SimpleEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.BlockDispenseArmorEvent")) {
            Skript.registerEvent("Dispenser - on Armor Dispense", SimpleEvent.class, BlockDispenseArmorEvent.class, "armor dispens(e|ing)")
                    .description("Called when an equippable item is dispensed from a block and equipped on a nearby entity.")
                    .examples("")
                    .since("1.0.0");
            EventValues.registerEventValue(BlockDispenseArmorEvent.class, Entity.class, new Getter<>() {
                @Override
                public Entity get(final BlockDispenseArmorEvent e) {
                    return e.getTargetEntity();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.player.PlayerChangedMainHandEvent")) {
            Skript.registerEvent("Player - on Main Hand Change", SimpleEvent.class, PlayerChangedMainHandEvent.class, "main hand switch")
                    .description("Called when a player changes their main hand in the client settings.")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("org.bukkit.event.inventory.PrepareGrindstoneEvent")) {
            Skript.registerEvent("Grindstone - on Prepare", SimpleEvent.class, PrepareGrindstoneEvent.class, "grindstone prepar(e[d]|ing)")
                    .description("Called when an item is put in a slot for repair or unenchanting in a grindstone.")
                    .examples("")
                    .since("1.0.0 (Paper), 1.0.3 (Spigot)");
            EventValues.registerEventValue(PrepareGrindstoneEvent.class, Block.class, new Getter<>() {
                @Override
                public Block get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    Location location = e.getInventory().getLocation();
                    if (location != null) {
                        return location.getBlock();
                    } else {
                        return null;
                    }
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PrepareGrindstoneEvent.class, Inventory.class, new Getter<>() {
                @Override
                public Inventory get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PrepareGrindstoneEvent.class, Location.class, new Getter<>() {
                @Override
                public Location get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory().getLocation();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PrepareGrindstoneEvent.class, Slot.class, new Getter<>() {
                @Override
                public Slot get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return new InventorySlot(e.getInventory(), 2);
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerBedFailEnterEvent")) {
            Skript.registerEvent("Player - on Sleep Fail", SimpleEvent.class, PlayerBedFailEnterEvent.class, "(sleep|bed [enter]) [attempt] fail", "fail[ed] to (sleep|enter [the] bed)")
                    .description("This Event requires Paper.\n\nCalled when a player attempts to sleep but fails..")
                    .examples("")
                    .since("1.0.0");
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(final PlayerBedFailEnterEvent e) {
                    return e.getBed();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Location.class, new Getter<>() {
                @Override
                public @NotNull Location get(final PlayerBedFailEnterEvent e) {
                    return e.getBed().getLocation();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent")) {
            Skript.registerEvent("Enderman - on Escape", SimpleEvent.class, EndermanEscapeEvent.class, "enderman escape")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when an enderman escapes.
                                                        
                            CRITICAL_HIT: The enderman has teleported away due to a critical hit
                            DROWN: Specific case for CRITICAL_HIT where the enderman is taking rain damage
                            INDIRECT: The enderman has teleported away due to indirect damage (ranged)
                            RUNAWAY: The enderman has stopped attacking and ran away
                            STARE: The enderman has teleported away due to the player staring at it during combat""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent")) {
            Skript.registerEvent("Enderman - on Attack Decide", SimpleEvent.class, EndermanAttackPlayerEvent.class, "enderman [attack] decide", "enderman decide to attack")
                    .description("""
                            This event requires Paper.
                                                        
                            Fired when an Enderman determines if it should attack a player or not.
                            Starts off cancelled if the player is wearing a pumpkin head or is not looking at the Enderman, according to Vanilla rules.""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("io.papermc.paper.event.player.PrePlayerAttackEntityEvent")) {
            Skript.registerEvent("Player - on Pre Damage", SimpleEvent.class, PrePlayerAttackEntityEvent.class, "pre[-| ]damage")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when the player tries to attack an entity. This occurs before any of the damage logic, so cancelling this event will prevent any sort of sounds from being played when attacking. This event will fire as cancelled for certain entities, use the "will be damaged" condition to check if the entity will not actually be attacked.
                            Note: there may be other factors (invulnerability, etc) that will prevent this entity from being attacked that this event will not cover.""")
                    .examples("")
                    .since("1.0.0");
            EventValues.registerEventValue(PrePlayerAttackEntityEvent.class, Entity.class, new Getter<>() {
                @Override
                public @NotNull Entity get(final PrePlayerAttackEntityEvent e) {
                    return e.getAttacked();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerPostRespawnEvent")) {
            Skript.registerEvent("Player - on Post-Respawn", SimpleEvent.class, PlayerPostRespawnEvent.class, "post[-| ]respawn")
                    .description("""
                            This event requires Paper.
                                                        
                            Fired after a player has respawned.""")
                    .examples("")
                    .since("1.0.0");
            EventValues.registerEventValue(PlayerPostRespawnEvent.class, Location.class, new Getter<>() {
                @Override
                public @NotNull Location get(final PlayerPostRespawnEvent e) {
                    return e.getRespawnedLocation();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceExtractEvent")) {
            Skript.registerEvent("Furnace - on Item Extract", SimpleEvent.class, FurnaceExtractEvent.class, "furnace extract")
                    .description("""
                            This event is called when a player takes items out of the furnace.""")
                    .examples("")
                    .since("1.0.1");
            EventValues.registerEventValue(FurnaceExtractEvent.class, Slot.class, new Getter<>() {
                @Override
                public @NotNull Slot get(final FurnaceExtractEvent e) {
                    InventoryView inventoryView = e.getPlayer().getOpenInventory();
                    return new InventorySlot(inventoryView.getTopInventory(), 2);
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(FurnaceExtractEvent.class, ItemType.class, new Getter<>() {
                @Override
                public @NotNull ItemType get(final FurnaceExtractEvent e) {
                    return new ItemType(e.getItemType());
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(FurnaceExtractEvent.class, Integer.class, new Getter<>() {
                @Override
                public @NotNull Integer get(final FurnaceExtractEvent e) {
                    return e.getItemAmount();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceStartSmeltEvent")) {
            Skript.registerEvent("Furnace - on Start Smelting", SimpleEvent.class, FurnaceStartSmeltEvent.class, "furnace start smelt[ing]")
                    .description("Called when a Furnace starts smelting.")
                    .examples("")
                    .since("1.0.1");
            EventValues.registerEventValue(FurnaceStartSmeltEvent.class, Integer.class, new Getter<>() {
                @Override
                public @NotNull Integer get(final FurnaceStartSmeltEvent e) {
                    return e.getTotalCookTime();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("io.papermc.paper.event.entity.WardenAngerChangeEvent")) {
            Skript.registerEvent("Warden - on Anger Change", SimpleEvent.class, WardenAngerChangeEvent.class, "warden anger change")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when a Warden's anger level has changed due to another entity.
                                                        
                            If the event is cancelled, the warden's anger level will not change.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("io.papermc.paper.event.block.CompostItemEvent")) {
            Skript.registerEvent("Composter - on Hopper Compost Item", SimpleEvent.class, CompostItemEvent.class, "hopper compost")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when an item is about to be composted by a hopper.
                            To prevent hoppers from moving items into composters, cancel the Inventory move event from SkBee.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("io.papermc.paper.event.entity.EntityCompostItemEvent")) {
            Skript.registerEvent("Composter - on Compost", SimpleEvent.class, EntityCompostItemEvent.class, "compost")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when an item is about to be composted by an entity.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent")) {
            Skript.registerEvent("Whitelist - on Verify", SimpleEvent.class, ProfileWhitelistVerifyEvent.class, "whitelist verify")
                    .description("""
                            This event requires Paper.
                                                        
                            Fires when the server needs to verify if a player is whitelisted. Plugins may override/control the servers whitelist with this event, and dynamically change the kick message.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(ProfileWhitelistVerifyEvent.class, OfflinePlayer.class, new Getter<>() {
                @Override
                public @Nullable OfflinePlayer get(final ProfileWhitelistVerifyEvent e) {
                    UUID id = e.getPlayerProfile().getId();
                    if (id != null) {
                        return Bukkit.getOfflinePlayer(id);
                    }
                    return null;
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockFailedDispenseEvent")) {
            Skript.registerEvent("Dispenser - on Dispense Fail", SimpleEvent.class, BlockFailedDispenseEvent.class, "dispense fail", "failed [to] dispense")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when a block tries to dispense an item, but its inventory is empty.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockPreDispenseEvent")) {
            Skript.registerEvent("Dispenser - on Pre Dispense", SimpleEvent.class, BlockPreDispenseEvent.class, "pre[-| ]dispense")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when a block is about to dispense an item.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BlockPreDispenseEvent.class, Integer.class, new Getter<>() {
                @Override
                public @NotNull Integer get(final BlockPreDispenseEvent e) {
                    return e.getSlot();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.block.BrewingStartEvent")) {
            Skript.registerEvent("Brewing Stand - on Brewing Start", SimpleEvent.class, BrewingStartEvent.class, "brewing [stand] (start[ing]|begin[ning])")
                    .description(" Called when a brewing stand starts to brew.")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BrewingStartEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @NotNull Inventory get(final BrewingStartEvent e) {
                    return ((InventoryHolder) e.getBlock()).getInventory();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(BrewingStartEvent.class, ItemType.class, new Getter<>() {
                @Override
                public @Nullable ItemType get(final BrewingStartEvent e) {
                    ItemStack itemStack = ((BrewingStand) e.getBlock()).getInventory().getIngredient();
                    if (itemStack != null) {
                        return new ItemType(itemStack);
                    }
                    return null;
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.inventory.BrewEvent")) {
            Skript.registerEvent("Brewing Stand - on Brew", SimpleEvent.class, BrewEvent.class, "[brewing stand] brew[ing]")
                    .description("Called when a Brewing Stand brews.")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BrewEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @NotNull Inventory get(final BrewEvent e) {
                    return e.getContents();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.inventory.BrewingStandFuelEvent")) {
            Skript.registerEvent("Brewing Stand - on Fuel", SimpleEvent.class, BrewingStandFuelEvent.class, "brewing [stand] fuel")
                    .description("Called when an ItemStack is about to increase the fuel level of a brewing stand.")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.block.CampfireStartEvent")) {
            Skript.registerEvent("Campfire - on Start", SimpleEvent.class, CampfireStartEvent.class, "campfire [start[ing]|begin[ning]]")
                    .description("Called when a Campfire starts to cook.")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.block.CauldronLevelChangeEvent")) {
            Skript.registerEvent("Cauldron - on Level Change", SimpleEvent.class, CauldronLevelChangeEvent.class, "cauldron [[level] change]")
                    .description("Called when a Cauldron's level changes.")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.entity.EnderDragonChangePhaseEvent")) {
            Skript.registerEvent("Ender Dragon - on Phase Change", SimpleEvent.class, EnderDragonChangePhaseEvent.class, "ender dragon phase change")
                    .description("Called when an EnderDragon changes phase.")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("io.papermc.paper.event.entity.ElderGuardianAppearanceEvent")) {
            Skript.registerEvent("Elder Guardian - on Appear", SimpleEvent.class, ElderGuardianAppearanceEvent.class, "elder guardian appear")
                    .description("""
                            This event requires Paper.
                            
                            Called when an ElderGuardian appears in front of a Player.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(ElderGuardianAppearanceEvent.class, Player.class, new Getter<>() {
                @Override
                public @NotNull Player get(final ElderGuardianAppearanceEvent e) {
                    return e.getAffectedPlayer();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            Skript.registerEvent("Entity - on Love Mode Enter", SimpleEvent.class, EntityEnterLoveModeEvent.class, "love [mode enter]")
                    .description("""
                            Called when an entity enters love mode.
                            This can be cancelled but the item will still be consumed that was used to make the entity enter into love mode.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(EntityEnterLoveModeEvent.class, CommandSender.class, new Getter<>() {
                @Override
                public @Nullable CommandSender get(final EntityEnterLoveModeEvent e) {
                    return e.getHumanEntity();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityPoseChangeEvent")) {
            Skript.registerEvent("Entity - on Pose Change", SimpleEvent.class, EntityPoseChangeEvent.class, "pose change")
                    .description("""
                            Called when an entity changes its pose.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(EntityPoseChangeEvent.class, Pose.class, new Getter<>() {
                @Override
                public @NotNull Pose get(final EntityPoseChangeEvent e) {
                    return e.getPose();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent")) {
            Skript.registerEvent("Entity - on Attack Push", SimpleEvent.class, EntityPushedByEntityAttackEvent.class, "(damage|attack) push")
                    .description("""
                            Fired when an entity is pushed by another entity's attack.
                            The acceleration vector can be modified. If this event is cancelled, the entity will not get pushed.
                                                        
                            Note: Some entities might trigger this multiple times on the same entity as multiple acceleration calculations are done.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(EntityPushedByEntityAttackEvent.class, Vector.class, new Getter<>() {
                @Override
                public @NotNull Vector get(final EntityPushedByEntityAttackEvent e) {
                    return e.getAcceleration();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.block.BellResonateEvent")) {
            Skript.registerEvent("Bell - on Resonate", SimpleEvent.class, BellResonateEvent.class, "bell resonate|raider[s] reveal")
                    .description("""
                            Called when a bell resonates after being rung and highlights nearby raiders.
                            A bell will only resonate if raiders are in the vicinity of the bell.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BellResonateEvent.class, LivingEntity[].class, new Getter<>() {
                @Override
                public LivingEntity @NotNull [] get(BellResonateEvent e) {
                    return e.getResonatedEntities().toArray(new LivingEntity[0]);
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.block.BellResonateEvent")) {
            Skript.registerEvent("Bell - on Ring", SimpleEvent.class, BellRingEvent.class, "bell ring")
                    .description("Called when a bell is being rung.")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BellRingEvent.class, BlockFace.class, new Getter<>() {
                @Override
                public @NotNull BlockFace get(final BellRingEvent e) {
                    return e.getDirection();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(BellRingEvent.class, Entity.class, new Getter<>() {
                @Override
                public @Nullable Entity get(final BellRingEvent e) {
                    return e.getEntity();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.block.FluidLevelChangeEvent")) {
            Skript.registerEvent("Block - on Fluid Level Change", SimpleEvent.class, FluidLevelChangeEvent.class, "fluid level change")
                    .description("""
                            Called when the fluid level of a block changes due to changes in adjacent blocks.""")
                    .examples("")
                    .since("1.0.4");
            EventValues.registerEventValue(FluidLevelChangeEvent.class, BlockData.class, new Getter<>() {
                @Override
                public @NotNull BlockData get(final FluidLevelChangeEvent e) {
                    return e.getNewData();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.player.PlayerAttemptPickupItemEvent")) {
            Skript.registerEvent("Player - on Pickup Attempt", SimpleEvent.class, PlayerAttemptPickupItemEvent.class, "[player] pickup attempt", "[player] attempt to pickup")
                    .description("""
                            Called when a player attempts to pick an item up from the ground.""")
                    .examples("")
                    .since("1.0.4");
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityShootBowEvent")) {
            Skript.registerEvent("Entity - on Shoot", SimpleEvent.class, EntityShootBowEvent.class, "entity shoot")
                    .description("""
                            Called when a LivingEntity shoots a bow firing an arrow.""")
                    .examples("")
                    .since("1.1.1");
            EventValues.registerEventValue(EntityShootBowEvent.class, Projectile.class, new Getter<>() {
                @Override
                public @Nullable Projectile get(EntityShootBowEvent event) {
                    return (event.getProjectile() instanceof Projectile projectile) ? projectile : null;
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityPlaceEvent")) {
            Skript.registerEvent("Entity - on Place", SimpleEvent.class, EntityPlaceEvent.class, "entity place")
                    .description("""
                            Triggered when an entity is created in the world by a player "placing" an item on a block.
                            Note that this event is currently only fired for four specific placements: armor stands, boats, minecarts, and end crystals.""")
                    .examples("")
                    .since("1.1.1");
            EventValues.registerEventValue(EntityPlaceEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(EntityPlaceEvent event) {
                    return event.getBlock();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(EntityPlaceEvent.class, Player.class, new Getter<>() {
                @Override
                public @Nullable Player get(EntityPlaceEvent event) {
                    return event.getPlayer();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(EntityPlaceEvent.class, BlockFace.class, new Getter<>() {
                @Override
                public @NotNull BlockFace get(EntityPlaceEvent event) {
                    return event.getBlockFace();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(EntityPlaceEvent.class, EquipmentSlot.class, new Getter<>() {
                @Override
                public @NotNull EquipmentSlot get(EntityPlaceEvent event) {
                    return event.getHand();
                }
            }, EventValues.TIME_NOW);
        }

    }
}
