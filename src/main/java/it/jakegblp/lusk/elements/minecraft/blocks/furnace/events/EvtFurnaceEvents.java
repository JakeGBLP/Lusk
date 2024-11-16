package it.jakegblp.lusk.elements.minecraft.blocks.furnace.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class EvtFurnaceEvents {
    static {
        if (Skript.classExists("org.bukkit.event.inventory.FurnaceExtractEvent")) {
            Skript.registerEvent("Furnace - on Item Extract", SimpleEvent.class, FurnaceExtractEvent.class, "furnace [item] extract[ed|ing]")
                    .description("""
                            This event is called when a player takes items out of the furnace.""")
                    .examples("")
                    .since("1.0.1");
            EventValues.registerEventValue(FurnaceExtractEvent.class, Slot.class, new Getter<>() {
                @Override
                public @NotNull Slot get(final FurnaceExtractEvent e) {
                    InventoryView inventoryView = e.getPlayer().getOpenInventory();
                    return new InventorySlot(inventoryView.getTopInventory(), 2);
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(FurnaceExtractEvent.class, ItemType.class, new Getter<>() {
                @Override
                public @NotNull ItemType get(final FurnaceExtractEvent e) {
                    return new ItemType(e.getItemType());
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(FurnaceExtractEvent.class, Integer.class, new Getter<>() {
                @Override
                public @NotNull Integer get(final FurnaceExtractEvent e) {
                    return e.getItemAmount();
                }
            }, EventValues.TIME_NOW);
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
            EventValues.registerEventValue(FurnaceStartSmeltEvent.class, Integer.class, new Getter<>() {
                @Override
                public @NotNull Integer get(final FurnaceStartSmeltEvent e) {
                    return e.getTotalCookTime();
                }
            }, EventValues.TIME_NOW);
        }
    }
}
