package it.jakegblp.lusk.elements.minecraft.entities.entity.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.papermc.paper.event.entity.EntityInsideBlockEvent;
import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EvtEntityEvents {
    static {
        if (Skript.classExists("org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            Skript.registerEvent("Entity - on Love Mode Enter", SimpleEvent.class, EntityEnterLoveModeEvent.class, "love [mode] (enter|start)[ed|ing]")
                    .description("""
                            Called when an entity enters love mode.
                            This can be cancelled but the used item will still be consumed.""")
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
            Skript.registerEvent("Entity - on Pose Change", SimpleEvent.class, EntityPoseChangeEvent.class, "pose chang(e[d]|ing)")
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
        if (Skript.classExists("org.bukkit.event.entity.EntityShootBowEvent")) {
            Skript.registerEvent("Entity - on Shoot", SimpleEvent.class, EntityShootBowEvent.class, "entity shoot[ing]")
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
            Skript.registerEvent("Entity - on Place", SimpleEvent.class, EntityPlaceEvent.class, "entity [getting] place[d]")
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
        if (Skript.classExists("io.papermc.paper.event.entity.EntityInsideBlockEvent")) {
            Skript.registerEvent("Entity - on Collide With Block", SimpleEvent.class, EntityInsideBlockEvent.class, "entity ((collide with|in[side]) [a] block)")
                    .description("""
                            Called when an entity enters the hitbox of a block. Only called for blocks that react when an entity is inside. If cancelled, any action that would have resulted from that entity being in the block will not happen (such as extinguishing an entity in a cauldron).
                            Blocks this is currently called for:

                            Big dripleaf
                            Bubble column
                            Buttons
                            Cactus
                            Campfire
                            Cauldron
                            Crops
                            Ender Portal
                            Fires
                            Frogspawn
                            Honey
                            Hopper
                            Detector rails
                            Nether portals
                            Powdered snow
                            Pressure plates
                            Sweet berry bush
                            Tripwire
                            Waterlily
                            Web
                            Wither rose""")
                    .examples("")
                    .since("1.1.1")
                    .requiredPlugins("Paper")
                    .documentationID("entity_-_inside_block_event");
        }
    }
}
