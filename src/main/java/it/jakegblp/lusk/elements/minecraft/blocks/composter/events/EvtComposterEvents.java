package it.jakegblp.lusk.elements.minecraft.blocks.composter.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import io.papermc.paper.event.block.CompostItemEvent;
import io.papermc.paper.event.entity.EntityCompostItemEvent;

public class EvtComposterEvents {
    static {
        if (Skript.classExists("io.papermc.paper.event.entity.EntityCompostItemEvent")) {
            Skript.registerEvent("Composter - on Compost", SimpleEvent.class, EntityCompostItemEvent.class, "compost")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when an item is about to be composted by an entity.""")
                    .examples("")
                    .since("1.0.1");
        }
        if (Skript.classExists("io.papermc.paper.event.block.CompostItemEvent")) {
            Skript.registerEvent("Composter - on Hopper Compost Item", SimpleEvent.class, CompostItemEvent.class, "hopper compost")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when an item is about to be composted by a hopper.
                            To prevent hoppers from moving items into composters, cancel the Inventory move event from SkBee.""")
                    .examples("")
                    .since("1.0.1");
        }
    }
}
