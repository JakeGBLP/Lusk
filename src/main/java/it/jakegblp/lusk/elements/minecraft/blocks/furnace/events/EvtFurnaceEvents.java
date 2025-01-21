package it.jakegblp.lusk.elements.minecraft.blocks.furnace.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;

import static it.jakegblp.lusk.utils.DeprecationUtils.registerEventValue;

@SuppressWarnings("unused")
public class EvtFurnaceEvents {
    static {
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceExtractEvent")) {
            Skript.registerEvent("Furnace - on Item Extract", SimpleEvent.class, FurnaceExtractEvent.class, "furnace [item] extract[ed|ing]")
                    .description("""
                            This event is called when a player takes items out of the furnace.
                            
                            event-integer -> item amount being retrieved
                            """)
                    .examples("")
                    .since("1.0.1");
            registerEventValue(FurnaceExtractEvent.class, Slot.class, e -> new InventorySlot(e.getPlayer().getOpenInventory().getTopInventory(), 2), EventValues.TIME_NOW);
            registerEventValue(FurnaceExtractEvent.class, ItemType.class, e -> new ItemType(e.getItemType()), EventValues.TIME_NOW);
            registerEventValue(FurnaceExtractEvent.class, Integer.class, FurnaceExtractEvent::getItemAmount, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceStartSmeltEvent")) {
            Skript.registerEvent("Furnace - on Start Smelting", SimpleEvent.class, FurnaceStartSmeltEvent.class, "furnace start[ed|ing] [to] smelt[ed|ing]", "furnace smelt[ed|ing] start[ed|ing]")
                    .description("Called when a Furnace starts smelting, or cooking for that matter.")
                    .examples("""
                            on furnace start smelting:
                              uncancel the event
                              broadcast "<bold>let him cook!"
                            """)
                    .since("1.0.1");
            registerEventValue(FurnaceStartSmeltEvent.class, Integer.class, FurnaceStartSmeltEvent::getTotalCookTime, EventValues.TIME_NOW);
        }
    }
}
