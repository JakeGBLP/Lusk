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
import it.jakegblp.lusk.utils.ClassUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Name("Used Hand")
@Description("The hand used in events where you might need to distinguish between the main and offhand.")
@Examples("set used hand to stone named \"no tool for you\"")
@Since("1.1")
public class ExprUsedHand extends SimpleExpression<Slot> {
    private static final ArrayList<Class<? extends Event>> cs = new ArrayList<>(List.of(PlayerUseUnknownEntityEvent.class, PlayerShearBlockEvent.class, EntityLoadCrossbowEvent.class, PlayerArmSwingEvent.class, BlockCanBuildEvent.class, BlockPlaceEvent.class, EntityPlaceEvent.class, EntityResurrectEvent.class, EntityShootBowEvent.class, PlayerLeashEntityEvent.class, PlayerArmorStandManipulateEvent.class, PlayerBucketEntityEvent.class, PlayerBucketEvent.class, PlayerFishEvent.class, PlayerHarvestBlockEvent.class, PlayerInteractEntityEvent.class, PlayerInteractEvent.class, PlayerItemConsumeEvent.class, PlayerShearEntityEvent.class, PlayerUnleashEntityEvent.class, HangingPlaceEvent.class));

    static {
        Skript.registerExpression(ExprUsedHand.class, Slot.class, ExpressionType.SIMPLE, "[the] [used|active] hand");
    }
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        for (Class<? extends Event> clazz : cs) {
            if (getParser().isCurrentEvent(clazz)) {
                return true;
            }
        }
        Skript.error("The Hand Expression can only be used in events where Hands are involved.", ErrorQuality.SEMANTIC_ERROR);
        return false;
    }

    @Override
    protected Slot @NotNull [] get(@NotNull Event e) {
        org.bukkit.inventory.EquipmentSlot slot = null;
        Class<? extends Event> lastClass = null;
        for (Class<? extends Event> clazz : cs) {
            if (!(Skript.methodExists(clazz,"getHand") && clazz.isInstance(e))) continue;
            try {
                lastClass = clazz;
                slot = (org.bukkit.inventory.EquipmentSlot) clazz.getMethod("getHand").invoke(e);
                break;
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (lastClass != null && slot != null) {
            if (ClassUtils.hasMethod(lastClass, "getPlayer")) {
                Player player;
                try {
                    player = (Player) lastClass.getMethod("getPlayer").invoke(e);
                } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }
                PlayerInventory inventory = player.getInventory();
                return new InventorySlot[]{new InventorySlot(inventory, slot == org.bukkit.inventory.EquipmentSlot.OFF_HAND ? 40 : inventory.getHeldItemSlot())};
            } else if (ClassUtils.hasMethod(lastClass, "getEntity")) {
                EntityEvent entityEvent = (EntityEvent) e;
                LivingEntity entity = (LivingEntity) entityEvent.getEntity();
                EntityEquipment entityEquipment = entity.getEquipment();
                if (entityEquipment == null)
                    return new EquipmentSlot[]{new EquipmentSlot(entity.getEquipment(),
                            slot ==  org.bukkit.inventory.EquipmentSlot.OFF_HAND ? EquipmentSlot.EquipSlot.OFF_HAND : EquipmentSlot.EquipSlot.TOOL)};
            }
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
        return "the used hand";
    }

}