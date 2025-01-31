package it.jakegblp.lusk.elements.minecraft.blocks.grindstone.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import org.bukkit.Location;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.Inventory;

import static it.jakegblp.lusk.utils.SkriptUtils.registerEventValue;

public class EvtGrindstoneEvents {
    static {
        if (Skript.classExists("org.bukkit.event.inventory.PrepareGrindstoneEvent")) {
            Skript.registerEvent("Grindstone - on Prepare", SimpleEvent.class, PrepareGrindstoneEvent.class, "grindstone prepar(e[d]|ing)")
                    .description("Called when an item is put in a slot for repair or unenchanting in a grindstone.")
                    .examples("")
                    .since("1.0.0 (Paper), 1.0.3 (Spigot)");
            registerEventValue(PrepareGrindstoneEvent.class, Inventory.class, PrepareGrindstoneEvent::getInventory, EventValues.TIME_NOW);
            registerEventValue(PrepareGrindstoneEvent.class, Location.class, e -> e.getInventory().getLocation(), EventValues.TIME_NOW);
            registerEventValue(PrepareGrindstoneEvent.class, Slot.class, e -> new InventorySlot(e.getInventory(), 2), EventValues.TIME_NOW);
        }
    }
}
