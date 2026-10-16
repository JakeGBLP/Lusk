package it.jakegblp.lusk.skript.modules.base.events;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;
import java.util.Locale;

public class EvtArmorStandManipulate extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.event(EvtBlockIgnite.class, BlockIgniteEvent.class)
                .patterns("armor[ |-]stand [%-*equipmentslots%] (:change|change:manipulate|:swap|:retrieve|:place)")
                .name("Armor Stand Manipulate")
                .description("""
                        Called when a player interacts with an armor stand and swaps, retrieves or places an item.
                        
                        `event-equipmentslot` is the clicked slot of the armor stand, to get the hand use the Used Hand expression
                        `event-entity` is the armor stand
                        
                        `past event-item` is the item the armor stand is holding
                        `future event-item` (or `event-item`) is the item the player is holding
                        """)
                .since("1.1.1")
                .keywords("armor", "stand", "armorstand", "manipulate", "change")
                .examples("""
                        on armor stand chest slot retrieve;
                            broadcast event-item and event-equipmentslot
                        """)
                .register(syntaxRegistry);
        Registrations.registerEventValue(PlayerArmorStandManipulateEvent.class, EquipmentSlot.class, PlayerArmorStandManipulateEvent::getSlot);
        Registrations.registerEventValue(PlayerArmorStandManipulateEvent.class, Entity.class, PlayerArmorStandManipulateEvent::getRightClicked);
        Registrations.registerEventValue(PlayerArmorStandManipulateEvent.class, LivingEntity.class, PlayerArmorStandManipulateEvent::getRightClicked);
        Registrations.registerFutureEventValue(PlayerArmorStandManipulateEvent.class, ItemType.class, e -> new ItemType(e.getPlayerItem()));
        Registrations.registerEventValue(PlayerArmorStandManipulateEvent.class, ItemType.class, e -> new ItemType(e.getArmorStandItem()));
    }

    public enum Interaction {
        CHANGE,
        SWAP,
        RETRIEVE,
        PLACE
    }

    private Literal<EquipmentSlot> equipmentSlotLiteral;
    private Interaction interaction;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        equipmentSlotLiteral = args[0];
        interaction = Interaction.valueOf(parseResult.tags.getFirst().toUpperCase(Locale.ENGLISH));
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        var armorStandManipulateEvent = (PlayerArmorStandManipulateEvent) event;
        if (Arrays.stream(equipmentSlotLiteral.getAll(event)).noneMatch(slot -> slot == armorStandManipulateEvent.getSlot())) return false;
        if (interaction == Interaction.CHANGE) return true;
        boolean toolIsAir = armorStandManipulateEvent.getPlayerItem().getType().isAir();
        boolean equippedIsAir = armorStandManipulateEvent.getArmorStandItem().getType().isAir();
        if (toolIsAir) {
            return equippedIsAir || interaction == Interaction.RETRIEVE;
        } else {
            return equippedIsAir ? interaction == Interaction.PLACE : interaction == Interaction.SWAP;
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "armor stand " + (equipmentSlotLiteral != null ? equipmentSlotLiteral.toString(e, debug) : "") + " " + interaction.name().toLowerCase(Locale.ENGLISH);
    }
}