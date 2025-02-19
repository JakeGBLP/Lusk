package it.jakegblp.lusk.elements.minecraft.blocks.smithingtable.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.Inventory;

import static it.jakegblp.lusk.utils.CompatibilityUtils.registerEventValue;

public class EvtSmithingTableEvents {
    static {
        if (Skript.classExists("org.bukkit.event.inventory.SmithItemEvent")) {
            Skript.registerEvent("Smithing Table - on Item Smith", SimpleEvent.class, SmithItemEvent.class,
                            "item smith[ed|ing]")
                    .description("Called when the recipe of an Item is completed inside a smithing table.")
                    .examples("on item smith:")
                    .since("1.3.4");
            registerEventValue(SmithItemEvent.class, Inventory.class, SmithItemEvent::getInventory, 0);
        }
    }
}
