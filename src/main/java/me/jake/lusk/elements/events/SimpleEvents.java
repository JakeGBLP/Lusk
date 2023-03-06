package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import io.papermc.paper.event.block.BellRingEvent;
import io.papermc.paper.event.player.PlayerBedFailEnterEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;

public class SimpleEvents {
    static {
        Skript.registerEvent("Armor Dispense", SimpleEvent.class, BlockDispenseArmorEvent.class, "armor dispens(e|ing)")
                .description("Called when an equippable item is dispensed from a block and equipped on a nearby entity.")
                .examples("")
                .since("1.0.0");
        Skript.registerEvent("Arrow Body Count Change", SimpleEvent.class, ArrowBodyCountChangeEvent.class, "arrow [body] count chang(e|ing)")
                .description("Called when an arrow enters or exists an entity's body.")
                .examples("")
                .since("1.0.0");
        Skript.registerEvent("Bat Toggle Sleep", SimpleEvent.class, BatToggleSleepEvent.class, "bat (toggle sleep|sleep toggle)")
                .description("Called when a bat attempts to sleep or wake up from its slumber.")
                .examples("")
                .since("1.0.0");
        if (Skript.classExists("com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent")) {
            Skript.registerEvent("Grindstone Prepare", SimpleEvent.class, PrepareGrindstoneEvent.class, "grindstone prepar(e[d]|ing)")
                    .description("Called when an item is put in a slot for repair or unenchanting in a grindstone.")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("io.papermc.paper.event.block.BellRingEvent")) {
            Skript.registerEvent("Bell Ring", SimpleEvent.class, BellRingEvent.class, "bell r(ung|ing[(ed|ing)])")
                    .description("This Event requires Paper.\n\nCalled when a bell is rung.")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerBedFailEnterEvent")) {
            Skript.registerEvent("Sleep Fail", SimpleEvent.class, PlayerBedFailEnterEvent.class, "(sleep|bed [enter]) [attempt] fail")
                    .description("This Event requires Paper.\n\nCalled when a player attempts to sleep but fails..")
                    .examples("")
                    .since("1.0.0");
        }
        Skript.registerEvent("Main Hand Switch", SimpleEvent.class, PlayerChangedMainHandEvent.class, "main hand switch")
                .description("Called when a player changes their main hand in the client settings.")
                .examples("")
                .since("1.0.0");
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent")) {
            Skript.registerEvent("Enderman Escape", SimpleEvent.class, EndermanEscapeEvent.class, "enderman escape")
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
            Skript.registerEvent("Enderman Decide", SimpleEvent.class, EndermanAttackPlayerEvent.class, "enderman [attack] decide")
                    .description("""
                            This event requires Paper.
                                                        
                            Fired when an Enderman determines if it should attack a player or not.
                            Starts off cancelled if the player is wearing a pumpkin head or is not looking at the Enderman, according to Vanilla rules.""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent")) {
            Skript.registerEvent("Player Start Spectating", SimpleEvent.class, PlayerStartSpectatingEntityEvent.class, "spectat(e|ing) [start|begin]")
                    .description("""
                            This event requires Paper.
                                                        
                            Triggered when a player starts spectating an entity in spectator mode.""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent")) {
            Skript.registerEvent("Player Stop Spectating", SimpleEvent.class, PlayerStopSpectatingEntityEvent.class, "spectat(e|ing) stop")
                    .description("""
                            This event requires Paper.
                                                        
                            Triggered when a player stops spectating an entity in spectator mode.""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("io.papermc.paper.event.player.PrePlayerAttackEntityEvent")) {
            Skript.registerEvent("Pre Damage Event", SimpleEvent.class, PrePlayerAttackEntityEvent.class, "pre[-| ]damage")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when the player tries to attack an entity. This occurs before any of the damage logic, so cancelling this event will prevent any sort of sounds from being played when attacking. This event will fire as cancelled for certain entities, use the "will be damaged" condition to check if the entity will not actually be attacked.
                            Note: there may be other factors (invulnerability, etc) that will prevent this entity from being attacked that this event will not cover.""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerPostRespawnEvent")) {
            Skript.registerEvent("Post Respawn", SimpleEvent.class, PlayerPostRespawnEvent.class, "(post|late[r]) respawn")
                    .description("""
                            This event requires Paper.
                                                        
                            Fired after a player has respawned.""")
                    .examples("")
                    .since("1.0.0");
        }
    }
}
