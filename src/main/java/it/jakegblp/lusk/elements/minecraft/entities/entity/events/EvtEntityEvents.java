package it.jakegblp.lusk.elements.minecraft.entities.entity.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import io.papermc.paper.event.entity.EntityInsideBlockEvent;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
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

import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.SkriptUtils.registerEventValue;
import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_19_2;
import static it.jakegblp.lusk.utils.Constants.PAPER_1_20_6;

public class EvtEntityEvents {
    static {
        if (Skript.classExists("org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            Skript.registerEvent("Entity - on Love Mode Enter", SimpleEvent.class, EntityEnterLoveModeEvent.class, "love [mode] (enter|start)[ed|ing]")
                    .description("""
                            Called when an entity enters love mode.
                            This can be cancelled but the used item will still be consumed.""")
                    .examples("")
                    .since("1.0.2");
            registerEventValue(EntityEnterLoveModeEvent.class, CommandSender.class, EntityEnterLoveModeEvent::getHumanEntity, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityPoseChangeEvent")) {
            Skript.registerEvent("Entity - on Pose Change", SimpleEvent.class, EntityPoseChangeEvent.class, "[entity] pose change[d]")
                    .description("""
                            Called when an entity changes its pose.
                            
                            Note: **`past event-pose` is the same as `pose of event-entity`, due to this it's not guaranteed that it will always work correctly.**
                            """)
                    .examples("")
                    .since("1.0.2, 1.3 (past event-pose)");
            registerEventValue(EntityPoseChangeEvent.class, Pose.class, e -> e.getEntity().getPose(), EventValues.TIME_PAST);
            registerEventValue(EntityPoseChangeEvent.class, Pose.class, EntityPoseChangeEvent::getPose, EventValues.TIME_NOW);
        }
        if (Skript.classExists("io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent")) {
            Skript.registerEvent("Entity - on Push by Entity Attack", SimpleEvent.class, EntityPushedByEntityAttackEvent.class, "(entity attack push|push by [entity] attack)")
                    .description("""
                            Fired when an entity is pushed by another entity's attack.
                            The acceleration vector can be modified. If this event is cancelled, the entity will not get pushed.
                            
                            Note: Some entities might trigger this multiple times on the same entity as multiple acceleration calculations are done.""")
                    .examples("on entity attack push:")
                    .requiredPlugins("Paper")
                    .since("1.0.2");
        }
        if (PAPER_1_20_6) {
            Skript.registerEvent("Entity - on Knockback", SimpleEvent.class, EntityKnockbackEvent.class, "entity knockback")
                    .description("""
                            Called when an entity receives knockback.""")
                    .examples("on entity knockback:")
                    .requiredPlugins("Paper")
                    .since("1.3.3");
            registerEventValue(EntityKnockbackEvent.class, Vector.class, EntityKnockbackEvent::getKnockback, EventValues.TIME_NOW);
        }
        // todo: merge most knockback events, support the deprecated spigot ones,
        //  support deprecated acceleration method.
        if (isPaper()) {
            Skript.registerEvent("Entity - on Knockback by Entity", SimpleEvent.class, EntityKnockbackByEntityEvent.class, "entity knockback by entity")
                    .description("""
                            Fired when an Entity is knocked back by the hit of another Entity.
                            The acceleration vector can be modified. If this event is cancelled, the entity is not knocked back.""")
                    .examples("on entity knockback by entity:")
                    .requiredPlugins("Paper")
                    .since("1.3.3");
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityShootBowEvent")) {
            Skript.registerEvent("Entity - on Shoot", SimpleEvent.class, EntityShootBowEvent.class, "entity shoot[ing]")
                    .description("""
                            Called when a LivingEntity shoots a bow firing an arrow.""")
                    .examples("")
                    .since("1.1.1");
            registerEventValue(EntityShootBowEvent.class, Projectile.class, e -> e.getProjectile() instanceof Projectile projectile ? projectile : null, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.entity.EntityPlaceEvent")) {
            Skript.registerEvent("Entity - on Place", SimpleEvent.class, EntityPlaceEvent.class, "entity [getting] place[d]")
                    .description("""
                            Triggered when an entity is created in the world by a player "placing" an item on a block.
                            Note that this event is currently only fired for four specific placements: armor stands, boats, minecarts, and end crystals.""")
                    .examples("")
                    .since("1.1.1");
            registerEventValue(EntityPlaceEvent.class, Block.class, EntityPlaceEvent::getBlock, EventValues.TIME_NOW);
            registerEventValue(EntityPlaceEvent.class, Player.class, EntityPlaceEvent::getPlayer, EventValues.TIME_NOW);
            registerEventValue(EntityPlaceEvent.class, BlockFace.class, EntityPlaceEvent::getBlockFace, EventValues.TIME_NOW);
            if (MINECRAFT_1_19_2)
                registerEventValue(EntityPlaceEvent.class, EquipmentSlot.class, EntityPlaceEvent::getHand, EventValues.TIME_NOW);
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
                    .documentationID("11161");
        }
    }
}
