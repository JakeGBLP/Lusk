package it.jakegblp.lusk.elements.minecraft.inventory.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

import static ch.njol.skript.util.slot.EquipmentSlot.EquipSlot.OFF_HAND;
import static ch.njol.skript.util.slot.EquipmentSlot.EquipSlot.TOOL;
import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Used Tool/Used Hand/Used Equipment Slot")
@Description("""
        Retrieves the used hand or item used in events where either hand is used.
        The first pattern returns the Slot (Item + Index) of the item in the used hand.
        - Note: using this is not the same as using `event-slot` or `event-item`, if either is available, use that
        The second pattern returns an Equipment Slot that's either `hand_slot` or `off_hand_slot`.
        
        Supported Events:
        - on Right Click
        - on Item Consume
        - on Arm Swing
        - on Entity Shoot (different than 'on Shoot'!)
        - on Crossbow Load
        - on Place (includes 'on Block Place', 'on Hanging Place', and 'on Entity Place')
        - on Resurrect
        - on Player Fish
        - on Bucket Fill
        - on Bucket Empty
        - on Elytra Boost
        - on Player Harvest
        - on Can Build Check
        - on Player Shear Entity
        - on Player Leash Entity
        - on Player Unleash Entity
        - on Player Use Unknown Entity
        """)
@Examples("broadcast event-used tool")
@Since("1.1, 1.1.1 (Optimized), 1.2 (Hand and Item, +1 Event, Fixes)")
@SuppressWarnings("unused")
public class ExprUsedTool extends SimpleExpression<Object> {
    private static final ArrayList<Class<? extends Event>> CLASSES = new ArrayList<>(List.of(BlockCanBuildEvent.class, BlockPlaceEvent.class, EntityPlaceEvent.class, EntityResurrectEvent.class, EntityShootBowEvent.class, PlayerLeashEntityEvent.class, PlayerArmorStandManipulateEvent.class, PlayerBucketEntityEvent.class, PlayerBucketEvent.class, PlayerFishEvent.class, PlayerHarvestBlockEvent.class, PlayerInteractEntityEvent.class, PlayerInteractEvent.class, PlayerItemConsumeEvent.class, PlayerShearEntityEvent.class, PlayerUnleashEntityEvent.class, HangingPlaceEvent.class)) {{
        if (Constants.PAPER_HAS_PLAYER_ARM_SWING_EVENT_HAND) add(PlayerArmSwingEvent.class);
        if (Constants.PAPER_HAS_ENTITY_LOAD_CROSSBOW_EVENT_HAND) add(EntityLoadCrossbowEvent.class);
        if (Constants.PAPER_HAS_PLAYER_ELYTRA_BOOST_EVENT_HAND) add(PlayerElytraBoostEvent.class);
        if (Constants.PAPER_HAS_PLAYER_SHEAR_BLOCK_EVENT_HAND) add(PlayerShearBlockEvent.class);
        if (Constants.PAPER_HAS_PLAYER_USE_UNKNOWN_ENTITY_EVENT_HAND) add(PlayerUseUnknownEntityEvent.class);
    }};

    //todo: should this return a skript slot?
    // an equipment slot?
    // i should make multiple patterns and an extensive description about this.
    static {
        Skript.registerExpression(ExprUsedTool.class, Object.class, EVENT_OR_SIMPLE, "[the] used (tool|[held] item|weapon)", "[[the] [used] |event-](hand [slot]|equipment[ ]slot)");
    }

    boolean hand;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        for (Class<? extends Event> clazz : CLASSES) {
            if (getParser().isCurrentEvent(clazz)) {
                hand = matchedPattern == 1;
                return true;
            }
        }
        Skript.error("The Hand Expression can only be used in events where Hands are involved.", ErrorQuality.SEMANTIC_ERROR);
        return false;
    }

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        // todo: add to utils?
        org.bukkit.inventory.EquipmentSlot slot = null;
        Player player = null;
        LivingEntity entity = null;
        if (e instanceof BlockCanBuildEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof BlockPlaceEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof EntityPlaceEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof EntityResurrectEvent event) {
            slot = event.getHand();
            entity = event.getEntity();
        } else if (e instanceof EntityShootBowEvent event) {
            slot = event.getHand();
            entity = event.getEntity();
        } else if (e instanceof PlayerLeashEntityEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof HangingPlaceEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerArmorStandManipulateEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerBucketEntityEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerBucketEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerFishEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerHarvestBlockEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerInteractEntityEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerInteractEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerItemConsumeEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerShearEntityEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (e instanceof PlayerUnleashEntityEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (Constants.PAPER_HAS_PLAYER_SHEAR_BLOCK_EVENT_HAND && e instanceof PlayerShearBlockEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (Constants.PAPER_HAS_ENTITY_LOAD_CROSSBOW_EVENT_HAND && e instanceof EntityLoadCrossbowEvent event) {
            slot = event.getHand();
            entity = event.getEntity();
        } else if (Constants.PAPER_HAS_PLAYER_ARM_SWING_EVENT_HAND && e instanceof PlayerArmSwingEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (Constants.PAPER_HAS_PLAYER_ELYTRA_BOOST_EVENT_HAND && e instanceof PlayerElytraBoostEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (Constants.PAPER_HAS_PLAYER_USE_UNKNOWN_ENTITY_EVENT_HAND && e instanceof PlayerUseUnknownEntityEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        }
        if (slot != null) {
            if (hand) return new EquipmentSlot[]{slot};
            if (player != null) {
                PlayerInventory inventory = player.getInventory();
                return new InventorySlot[]{new InventorySlot(inventory, slot == EquipmentSlot.OFF_HAND ? 40 : inventory.getHeldItemSlot())};
            } else if (entity != null) {
                EntityEquipment entityEquipment = entity.getEquipment();
                if (entityEquipment != null)
                    return new ch.njol.skript.util.slot.EquipmentSlot[]{new ch.njol.skript.util.slot.EquipmentSlot(entityEquipment,
                            slot == EquipmentSlot.OFF_HAND ? OFF_HAND : TOOL)};
            }
        }
        return new Object[0];
    }

    @Override
    public @NotNull Class<? extends Slot> getReturnType() {
        return Slot.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the used " + (hand ? "hand" : "tool");
    }
}