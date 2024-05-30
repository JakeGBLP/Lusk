package it.jakegblp.lusk.elements.minecraft.blocks.cauldron.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;

public class EvtCauldronEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.CauldronLevelChangeEvent")) {
            Skript.registerEvent("Cauldron - on Level Change", SimpleEvent.class, CauldronLevelChangeEvent.class, "cauldron [[level] chang(e[d]|ing)]")
                    .description("Called when a Cauldron's level changes.")
                    .examples("")
                    .since("1.0.2");
        }
    }
}
