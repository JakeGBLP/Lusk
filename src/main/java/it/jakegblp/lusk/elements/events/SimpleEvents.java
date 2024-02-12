package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
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
import it.jakegblp.lusk.events.PlayerEntityCollideEvent;
import it.jakegblp.lusk.events.PlayerShieldUpEvent;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;

@SuppressWarnings({"unused", "deprecation"})
public class SimpleEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.BlockDispenseArmorEvent")) {
            Skript.registerEvent("Dispenser - Armor Dispense", SimpleEvent.class, BlockDispenseArmorEvent.class, "armor dispens(e|ing)")
                    .description("Called when an equippable item is dispensed from a block and equipped on a nearby entity.")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("org.bukkit.event.player.PlayerChangedMainHandEvent")) {
            Skript.registerEvent("Player - Main Hand Change", SimpleEvent.class, PlayerChangedMainHandEvent.class, "main hand switch")
                    .description("Called when a player changes their main hand in the client settings.")
                    .examples("")
                    .since("1.0.0");
        }
        Class<? extends Event> prepareGrindstoneEvent = null;
        if (Skript.classExists("org.bukkit.event.inventory.PrepareGrindstoneEvent")) {
            prepareGrindstoneEvent = org.bukkit.event.inventory.PrepareGrindstoneEvent.class;
        } else if (Skript.classExists("com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent")) {
            prepareGrindstoneEvent = com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent.class;
        }
        if (prepareGrindstoneEvent != null) {
            Skript.registerEvent("Grindstone - Prepare", SimpleEvent.class, prepareGrindstoneEvent, "grindstone prepar(e[d]|ing)")
                    .description("Called when an item is put in a slot for repair or unenchanting in a grindstone.")
                    .examples("")
                    .since("1.0.0 (Paper), 1.0.3 (Spigot)");
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerBedFailEnterEvent")) {
            Skript.registerEvent("Player - Sleep Fail", SimpleEvent.class, PlayerBedFailEnterEvent.class, "(sleep|bed [enter]) [attempt] fail")
                    .description("This Event requires Paper.\n\nCalled when a player attempts to sleep but fails..")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent")) {
            Skript.registerEvent("Enderman - Escape", SimpleEvent.class, EndermanEscapeEvent.class, "enderman escape")
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
            Skript.registerEvent("Enderman - Decide", SimpleEvent.class, EndermanAttackPlayerEvent.class, "enderman [attack] decide")
                    .description("""
                            This event requires Paper.
                                                        
                            Fired when an Enderman determines if it should attack a player or not.
                            Starts off cancelled if the player is wearing a pumpkin head or is not looking at the Enderman, according to Vanilla rules.""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("io.papermc.paper.event.player.PrePlayerAttackEntityEvent")) {
            Skript.registerEvent("Player - Pre Damage", SimpleEvent.class, PrePlayerAttackEntityEvent.class, "pre[-| ]damage")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when the player tries to attack an entity. This occurs before any of the damage logic, so cancelling this event will prevent any sort of sounds from being played when attacking. This event will fire as cancelled for certain entities, use the "will be damaged" condition to check if the entity will not actually be attacked.
                            Note: there may be other factors (invulnerability, etc) that will prevent this entity from being attacked that this event will not cover.""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerPostRespawnEvent")) {
            Skript.registerEvent("Player - Post Respawn", SimpleEvent.class, PlayerPostRespawnEvent.class, "(post|late[r]) respawn")
                    .description("""
                            This event requires Paper.
                                                        
                            Fired after a player has respawned.""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceExtractEvent")) {
            Skript.registerEvent("Furnace - Extract", SimpleEvent.class, FurnaceExtractEvent.class, "furnace extract")
                    .description("""
                            This event is called when a player takes items out of the furnace.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceStartSmeltEvent")) {
            Skript.registerEvent("Furnace - Start Smelting", SimpleEvent.class, FurnaceStartSmeltEvent.class, "furnace start smelt[ing]")
                    .description("""
                            Called when a Furnace starts smelting.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("io.papermc.paper.event.entity.WardenAngerChangeEvent")) {
            Skript.registerEvent("Warden - Anger Change", SimpleEvent.class, WardenAngerChangeEvent.class, "warden anger change")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when a Warden's anger level has changed due to another entity.
                                                        
                            If the event is cancelled, the warden's anger level will not change.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("io.papermc.paper.event.block.CompostItemEvent")) {
            Skript.registerEvent("Composter - Hopper Compost Item", SimpleEvent.class, CompostItemEvent.class, "hopper compost")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when an item is about to be composted by a hopper.
                            To prevent hoppers from moving items into composters, cancel the Inventory move event from SkBee.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("io.papermc.paper.event.entity.EntityCompostItemEvent")) {
            Skript.registerEvent("Composter - Pre Compost", SimpleEvent.class, EntityCompostItemEvent.class, "compost")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when an item is about to be composted by an entity.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent")) {
            Skript.registerEvent("Whitelist - Verify", SimpleEvent.class, ProfileWhitelistVerifyEvent.class, "whitelist verify")
                    .description("""
                            This event requires Paper.
                                                        
                            Fires when the server needs to verify if a player is whitelisted. Plugins may override/control the servers whitelist with this event, and dynamically change the kick message.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockFailedDispenseEvent")) {
            Skript.registerEvent("Dispenser - Dispense Fail", SimpleEvent.class, BlockFailedDispenseEvent.class, "dispense fail", "failed dispense")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when a block tries to dispense an item, but its inventory is empty.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockPreDispenseEvent")) {
            Skript.registerEvent("Dispenser - Pre Dispense", SimpleEvent.class, BlockPreDispenseEvent.class, "pre dispense")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when a block is about to dispense an item.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.block.BrewingStartEvent")) {
            Skript.registerEvent("Brewing Stand - Start", SimpleEvent.class, BrewingStartEvent.class, "brewing [stand] (start[ing]|begin[ning])")
                    .description("""
                            Called when a brewing stand starts to brew.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.inventory.BrewEvent")) {
            Skript.registerEvent("Brewing Stand - Brew", SimpleEvent.class, BrewEvent.class, "[brewing stand] brew")
                    .description("""
                            Called when a Brewing Stand brews.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.inventory.BrewingStandFuelEvent")) {
            Skript.registerEvent("Brewing Stand - Fuel", SimpleEvent.class, BrewingStandFuelEvent.class, "brewing stand fuel")
                    .description("""
                            Called when an ItemStack is about to increase the fuel level of a brewing stand.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.block.CampfireStartEvent")) {
            Skript.registerEvent("Campfire - Start", SimpleEvent.class, CampfireStartEvent.class, "campfire [start[ing]|begin[ning]]")
                    .description("""
                            Called when a Campfire starts to cook.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.block.CauldronLevelChangeEvent")) {
            Skript.registerEvent("Cauldron - Level Change", SimpleEvent.class, CauldronLevelChangeEvent.class, "cauldron [level change]")
                    .description("""
                            Called when a Cauldron's level changes.'""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.entity.EnderDragonChangePhaseEvent")) {
            Skript.registerEvent("Ender Dragon - Change Phase", SimpleEvent.class, EnderDragonChangePhaseEvent.class, "ender dragon phase change")
                    .description("""
                            Called when an EnderDragon switches phase.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("io.papermc.paper.event.entity.ElderGuardianAppearanceEvent")) {
            Skript.registerEvent("Elder Guardian - Appear", SimpleEvent.class, ElderGuardianAppearanceEvent.class, "elder guardian appear")
                    .description("""
                            Called when an ElderGuardian appears in front of a Player.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            Skript.registerEvent("Entity - Love Mode Enter", SimpleEvent.class, EntityEnterLoveModeEvent.class, "love [mode enter]")
                    .description("""
                            Called when an entity enters love mode.
                            This can be cancelled but the item will still be consumed that was used to make the entity enter into love mode.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityPoseChangeEvent")) {
            Skript.registerEvent("Entity - Pose Change", SimpleEvent.class, EntityPoseChangeEvent.class, "pose change")
                    .description("""
                            Called when an entity changes its pose.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent")) {
            Skript.registerEvent("Entity - Attack Push", SimpleEvent.class, EntityPushedByEntityAttackEvent.class, "(damage|attack) push")
                    .description("""
                            Fired when an entity is pushed by another entity's attack.
                            The acceleration vector can be modified. If this event is cancelled, the entity will not get pushed.
                                                        
                            Note: Some entities might trigger this multiple times on the same entity as multiple acceleration calculations are done.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.block.BellResonateEvent")) {
            Skript.registerEvent("Bell - Resonate", SimpleEvent.class, BellResonateEvent.class, "bell resonate")
                    .description("""
                            Called when a bell resonated after being rung and highlights nearby raiders.
                            A bell will only resonate if raiders are in the vicinity of the bell.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("org.bukkit.event.block.FluidLevelChangeEvent")) {
            Skript.registerEvent("Block - Fluid Level Change", SimpleEvent.class, FluidLevelChangeEvent.class, "fluid level change")
                    .description("""
                            Called when the fluid level of a block changes due to changes in adjacent blocks.""")
                    .examples("")
                    .since("1.0.4");
        }
        if (Skript.classExists("org.bukkit.event.player.PlayerAttemptPickupItemEvent")) {
            Skript.registerEvent("Player - Pickup Attempt", SimpleEvent.class, PlayerAttemptPickupItemEvent.class, "[player] pickup attempt", "[player] attempt to pickup")
                    .description("""
                            Called when a player attempts to pick an item up from the ground.""")
                    .examples("")
                    .since("1.0.4");
        }
        Skript.registerEvent("Player - Entity Collide", SimpleEvent.class, PlayerEntityCollideEvent.class, "player collide [with entity]")
                .description("""
                        Called when a player collides with another entity.""")
                .examples("")
                .since("1.0.4");
        Skript.registerEvent("Player - Shield Raise", SimpleEvent.class, PlayerShieldUpEvent.class, "[player] shield (up|on|(use|raise)[d])")
                .description("""
                        Called when a player starts defending with a shield.
                        This event is called right before the shield is used, to use the is defending condition: wait a tick.""")
                .examples("")
                .since("1.0.4");
    }
}
