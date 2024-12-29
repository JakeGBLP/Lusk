package it.jakegblp.lusk.elements.minecraft.entities.player.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
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

        EventValues.registerEventValue(PlayerInteractEvent.class, EquipmentSlot.class, new Getter<>() {
            @Override
            public @Nullable EquipmentSlot get(PlayerInteractEvent event) {
                return event.getHand();
            }
        }, EventValues.TIME_NOW);

        EventValues.registerEventValue(PlayerInteractEvent.class, Action.class, new Getter<>() {
            @Override
            public Action get(PlayerInteractEvent event) {
                return event.getAction();
            }
        }, EventValues.TIME_NOW);

        EventValues.registerEventValue(PlayerInteractEvent.class, Vector.class, new Getter<>() {
            @Override
            public @Nullable Vector get(PlayerInteractEvent event) {
                if (!isPaper()) return event.getClickedPosition();
                Location interactionPoint = event.getInteractionPoint();
                if (interactionPoint == null) return null;
                return interactionPoint.getDirection().subtract(event.getPlayer().getLocation().getDirection());
            }
        }, EventValues.TIME_NOW);

        EventValues.registerEventValue(PlayerInteractEvent.class, Location.class, new Getter<>() {
            @Override
            public @Nullable Location get(PlayerInteractEvent event) {
                if (isPaper()) return event.getInteractionPoint();
                Vector offset = event.getClickedPosition();
                if (offset == null) return null;
                return event.getPlayer().getLocation().add(offset);
            }
        }, EventValues.TIME_NOW);

        EventValues.registerEventValue(PlayerInteractEntityEvent.class, EquipmentSlot.class, new Getter<>() {
            @Override
            public EquipmentSlot get(PlayerInteractEntityEvent event) {
                return event.getHand();
            }
        }, EventValues.TIME_NOW);

        EventValues.registerEventValue(PlayerInteractAtEntityEvent.class, Location.class, new Getter<>() {
            @Override
            public Location get(PlayerInteractAtEntityEvent event) {
                return event.getRightClicked().getLocation().add(event.getClickedPosition());
            }
        }, EventValues.TIME_NOW);

        EventValues.registerEventValue(PlayerInteractAtEntityEvent.class, Vector.class, new Getter<>() {
            @Override
            public Vector get(PlayerInteractAtEntityEvent event) {
                return event.getClickedPosition();
            }
        }, EventValues.TIME_NOW);
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
