package it.jakegblp.lusk.elements.minecraft.entities.armorstand.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import it.jakegblp.lusk.api.enums.ArmorStandInteraction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static it.jakegblp.lusk.utils.EventUtils.getInteraction;

@SuppressWarnings("unused")
public class EvtArmorStandManipulate extends SkriptEvent {

    static {
        if (Skript.classExists("org.bukkit.event.player.PlayerArmorStandManipulateEvent")) {
            Skript.registerEvent("Armor Stand - on Manipulate", EvtArmorStandManipulate.class, PlayerArmorStandManipulateEvent.class,
                            "armor[ |-]stand [%-*equipmentslots%] %*armorstandinteractions%")
                    .description("""
                            Called when a player interacts with an armor stand and will either swap, retrieve, place an item.
                            
                            `event-equipmentslot` is the clicked slot of the armor stand, to get the hand use the Used Hand expression
                            `event-entity` is the armor stand
                            
                            `past event-item` is the item the armor stand is holding
                            `future event-item` (or `event-item`) is the item the player is holding
                            
                            `event-armorstand interaction` is the type of manipulation that's happening during the event
                            (NOTE: this will only ever return `Place`, `Retrieve` or `Change`)
                            """)
                    .examples("""
                            on armor stand chest slot retrieve;
                                broadcast event-item and event-equipmentslot
                            """)
                    .since("1.1.1");
            EventValues.registerEventValue(PlayerArmorStandManipulateEvent.class, EquipmentSlot.class, new Getter<>() {
                @Override
                public EquipmentSlot get(final PlayerArmorStandManipulateEvent e) {
                    return e.getSlot();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PlayerArmorStandManipulateEvent.class, Entity.class, new Getter<>() {
                @Override
                public Entity get(final PlayerArmorStandManipulateEvent e) {
                    return e.getRightClicked();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PlayerArmorStandManipulateEvent.class, LivingEntity.class, new Getter<>() {
                @Override
                public LivingEntity get(final PlayerArmorStandManipulateEvent e) {
                    return e.getRightClicked();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PlayerArmorStandManipulateEvent.class, ItemType.class, new Getter<>() {
                @Override
                public ItemType get(final PlayerArmorStandManipulateEvent e) {
                    return new ItemType(e.getPlayerItem());
                }
            }, EventValues.TIME_FUTURE);
            EventValues.registerEventValue(PlayerArmorStandManipulateEvent.class, ItemType.class, new Getter<>() {
                @Override
                public ItemType get(final PlayerArmorStandManipulateEvent e) {
                    return new ItemType(e.getArmorStandItem());
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PlayerArmorStandManipulateEvent.class, ArmorStandInteraction.class, new Getter<>() {
                @Override
                public ArmorStandInteraction get(final PlayerArmorStandManipulateEvent e) {
                    return getInteraction(e);
                }
            }, EventValues.TIME_NOW);
        }
    }

    private Literal<EquipmentSlot> slots;
    private Literal<ArmorStandInteraction> interactionLiteral;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        slots = args[0];
        interactionLiteral = args[1];
        List<ArmorStandInteraction> list = Arrays.stream(interactionLiteral.getAll()).toList();
        if (list.contains(ArmorStandInteraction.CHANGE) && list.size() > 1) {
            Skript.warning("The generic interaction type covers all cases, the rest of the interactions added here are redundant.");
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        PlayerArmorStandManipulateEvent event = (PlayerArmorStandManipulateEvent) e;
        boolean toolIsAir = event.getPlayerItem().getType().isAir();
        boolean equippedIsAir = event.getArmorStandItem().getType().isAir();
        List<ArmorStandInteraction> list = Arrays.stream(interactionLiteral.getAll()).toList();
        if (list.contains(ArmorStandInteraction.CHANGE) || list.contains(getInteraction(event))) {
            if (slots != null)
                return slots.check(event, slot -> slot.equals(event.getSlot()));
            return true;
        }
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "armor stand " + (slots != null ? slots.toString(e, debug) : "") + " " + (interactionLiteral != null ? interactionLiteral.toString(e, debug) : "");
    }
}
