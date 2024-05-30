package it.jakegblp.lusk.elements.minecraft.blocks.campfire.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.event.block.CampfireStartEvent;

public class EvtCampfireEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.CampfireStartEvent")) {
            Skript.registerEvent("Campfire - on Start", SimpleEvent.class, CampfireStartEvent.class, "campfire start[ed|ing] [to cook]","campfire beg(an|un|in[ning]")
                    .description("Called when a Campfire starts to cook.")
                    .examples("")
                    .since("1.0.2");
        }
    }
}
