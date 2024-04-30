package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.util.slot.EquipmentSlot;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import it.jakegblp.lusk.Lusk;
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
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//

@Name("Used Tool")
@Description("The held item used in events where you might need to distinguish between the main and offhand.")
@Examples("broadcast event-used tool")
@Since("1.1")
public class ExprUsedTool extends SimpleExpression<Slot> {
    private static final ArrayList<Class<? extends Event>> cs = new ArrayList<>(List.of(PlayerUseUnknownEntityEvent.class, PlayerShearBlockEvent.class, EntityLoadCrossbowEvent.class, PlayerArmSwingEvent.class, BlockCanBuildEvent.class, BlockPlaceEvent.class, EntityPlaceEvent.class, EntityResurrectEvent.class, EntityShootBowEvent.class, PlayerLeashEntityEvent.class, PlayerArmorStandManipulateEvent.class, PlayerBucketEntityEvent.class, PlayerBucketEvent.class, PlayerFishEvent.class, PlayerHarvestBlockEvent.class, PlayerInteractEntityEvent.class, PlayerInteractEvent.class, PlayerItemConsumeEvent.class, PlayerShearEntityEvent.class, PlayerUnleashEntityEvent.class, HangingPlaceEvent.class));

    static {
        Skript.registerExpression(ExprUsedTool.class, Slot.class, ExpressionType.SIMPLE, "[the] used (tool|[held ]item)");
    }
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        for (Class<? extends Event> clazz : cs) {
            if (getParser().isCurrentEvent(clazz)) {
                return true;
            }
        }
        Lusk.getInstance().getLogger().info(Arrays.toString(getParser().getCurrentEvents()));
        Skript.error("The Hand Expression can only be used in events where Hands are involved.", ErrorQuality.SEMANTIC_ERROR);
        return false;
    }

    @Override
    protected Slot @NotNull [] get(@NotNull Event e) {
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
        } else if (Skript.classExists("io.papermc.paper.event.block.PlayerShearBlockEvent") && e instanceof PlayerShearBlockEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (Skript.classExists("io.papermc.paper.event.entity.EntityLoadCrossbowEvent") && e instanceof EntityLoadCrossbowEvent event) {
            slot = event.getHand();
            entity = event.getEntity();
        } else if (Skript.classExists("io.papermc.paper.event.player.PlayerArmSwingEvent") && e instanceof PlayerArmSwingEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        } else if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent") && e instanceof PlayerUseUnknownEntityEvent event) {
            player = event.getPlayer();
            slot = event.getHand();
        }
        if (slot != null)
            if (player != null) {
                PlayerInventory inventory = player.getInventory();
                return new InventorySlot[]{new InventorySlot(inventory,slot == org.bukkit.inventory.EquipmentSlot.OFF_HAND ? 40 : inventory.getHeldItemSlot())};
            } else if (entity != null) {
                EntityEquipment entityEquipment = entity.getEquipment();
                if (entityEquipment != null)
                    return new EquipmentSlot[]{new EquipmentSlot(entityEquipment,
                            slot ==  org.bukkit.inventory.EquipmentSlot.OFF_HAND ? EquipmentSlot.EquipSlot.OFF_HAND : EquipmentSlot.EquipSlot.TOOL)};
            }
        return new Slot[0];
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
        return "the used tool";
    }

}