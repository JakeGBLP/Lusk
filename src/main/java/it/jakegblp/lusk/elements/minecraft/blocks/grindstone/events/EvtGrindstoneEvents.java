package it.jakegblp.lusk.elements.minecraft.blocks.grindstone.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.Inventory;

public class EvtGrindstoneEvents {
    static {
        if (Skript.classExists("org.bukkit.event.inventory.PrepareGrindstoneEvent")) {
            Skript.registerEvent("Grindstone - on Prepare", SimpleEvent.class, PrepareGrindstoneEvent.class, "grindstone prepar(e[d]|ing)")
                    .description("Called when an item is put in a slot for repair or unenchanting in a grindstone.")
                    .examples("")
                    .since("1.0.0 (Paper), 1.0.3 (Spigot)");
            EventValues.registerEventValue(PrepareGrindstoneEvent.class, Block.class, new Getter<>() {
                @Override
                public Block get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    Location location = e.getInventory().getLocation();
                    if (location != null) {
                        return location.getBlock();
                    } else {
                        return null;
                    }
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PrepareGrindstoneEvent.class, Inventory.class, new Getter<>() {
                @Override
                public Inventory get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PrepareGrindstoneEvent.class, Location.class, new Getter<>() {
                @Override
                public Location get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return e.getInventory().getLocation();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PrepareGrindstoneEvent.class, Slot.class, new Getter<>() {
                @Override
                public Slot get(final org.bukkit.event.inventory.PrepareGrindstoneEvent e) {
                    return new InventorySlot(e.getInventory(), 2);
                }
            }, EventValues.TIME_NOW);
        }
    }
}
