package it.jakegblp.lusk.elements.minecraft.blocks.smithingtable.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.event.inventory.SmithItemEvent;

public class EvtSmithingTableEvents {
    static {
        if (Skript.classExists("org.bukkit.event.inventory.SmithItemEvent")) {
            Skript.registerEvent("Smithing Table - on Item Smith", SimpleEvent.class, SmithItemEvent.class,
                            "item smith[ed|ing]")
                    .description("Called when the recipe of an Item is completed inside a smithing table.")
                    .examples("on item smith:")
                    .since("1.3.4");
        }
    }
}
