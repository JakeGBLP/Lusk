package it.jakegblp.lusk.elements.minecraft.entities.player.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import it.jakegblp.lusk.utils.LuskUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.DeprecationUtils.registerEventValue;

@SuppressWarnings("deprecation")
public class EvtPlayerInteract extends SkriptEvent {

    static {
        Skript.registerEvent("Player - on Interact", EvtPlayerInteract.class,
                CollectionUtils.array(PlayerInteractEvent.class, PlayerInteractAtEntityEvent.class),
                        "player [[:main|:off][ |-]hand [slot]] interact[ing|ion] [entity:(on|with|at) [an] entity|block:(with|on) [a] block]")
                .description("""
                        Called when a player interacts with a block or entity by clicking.
                        
                        This event is always called unlike Skript's click event, meaning that it can be called for both hands.
                        
                        You can use `main hand` and `off hand` to make it per hand.
                        
                        `event-equipmentslot` = the slot of the used hand
                        `event-blockaction` = the action of this event
                        `event-vector` = the vector from the player to the interaction point
                        `event-location` = the location of the interaction point
                        """)
                .examples("on player main hand interaction:", "on player interacting with an entity:\n\tif event-equipmentslot = hand slot:")
                .since("1.3");

        registerEventValue(PlayerInteractEvent.class, EquipmentSlot.class, PlayerInteractEvent::getHand, EventValues.TIME_NOW);
        registerEventValue(PlayerInteractEvent.class, Action.class, PlayerInteractEvent::getAction, EventValues.TIME_NOW);
        registerEventValue(PlayerInteractEvent.class, Vector.class, e -> {
            if (!isPaper()) return e.getClickedPosition();
            Location interactionPoint = e.getInteractionPoint();
            if (interactionPoint == null) return null;
            return interactionPoint.getDirection().subtract(e.getPlayer().getLocation().getDirection());
        }, EventValues.TIME_NOW);
        registerEventValue(PlayerInteractEvent.class, Location.class, e -> {
                if (isPaper()) return e.getInteractionPoint();
                Vector offset = e.getClickedPosition();
                if (offset == null) return null;
                return e.getPlayer().getLocation().add(offset);
        }, EventValues.TIME_NOW);
        registerEventValue(PlayerInteractEntityEvent.class, EquipmentSlot.class, PlayerInteractEntityEvent::getHand, EventValues.TIME_NOW);
        registerEventValue(PlayerInteractAtEntityEvent.class, Location.class, e -> e.getRightClicked().getLocation().add(e.getClickedPosition()), EventValues.TIME_NOW);
        registerEventValue(PlayerInteractAtEntityEvent.class, Vector.class, PlayerInteractAtEntityEvent::getClickedPosition, EventValues.TIME_NOW);
    }

    private Kleenean isEntityInteraction;
    private Kleenean isMainHandInteraction;

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        isEntityInteraction = LuskUtils.getKleenean(parseResult.hasTag("entity"), parseResult.hasTag("block"));
        isMainHandInteraction = LuskUtils.getKleenean(parseResult.hasTag("main"), parseResult.hasTag("off"));
        return true;
    }

    @Override
    public boolean check(Event e) {
        boolean result = false;
        EquipmentSlot hand = null;
        if (!isEntityInteraction.isFalse() && e instanceof PlayerInteractAtEntityEvent event) {
            result = true;
            hand = event.getHand();
        } else if (!isEntityInteraction.isTrue() && e instanceof PlayerInteractEvent event) {
            result = true;
            hand = event.getHand();
        }
        if (!result || hand == null) return result;
        return switch (isMainHandInteraction) {
            case TRUE -> hand.equals(EquipmentSlot.HAND);
            case FALSE -> hand.equals(EquipmentSlot.OFF_HAND);
            case UNKNOWN -> true;
        };
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "player " + (switch (isMainHandInteraction) {
            case TRUE -> "main hand ";
            case FALSE -> "off hand ";
            case UNKNOWN -> "";
        }) + "interaction" + switch (isEntityInteraction) {
            case TRUE -> " with an entity";
            case FALSE -> " with a block";
            case UNKNOWN -> "";
        };
    }
}
