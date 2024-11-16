package it.jakegblp.lusk.elements.minecraft.entities.warden.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import io.papermc.paper.event.entity.WardenAngerChangeEvent;

@SuppressWarnings("unused")
public class EvtWardenEvents {
    static {
        if (Skript.classExists("io.papermc.paper.event.entity.WardenAngerChangeEvent")) {
            Skript.registerEvent("Warden - on Anger Change", SimpleEvent.class, WardenAngerChangeEvent.class, "warden anger chang(e[d]|ing)")
                    .description("""
                            Called when a Warden's anger level has changed due to another entity.

                            If the event is cancelled, the warden's anger level will not change.""")
                    .examples("")
                    .since("1.0.1")
                    .requiredPlugins("Paper");
        }
    }
}
