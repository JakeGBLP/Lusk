package it.jakegblp.lusk.elements.minecraft.entities.hostile.enderdragon.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;

public class EvtEnderDragonEvents {
    static {
        if (Skript.classExists("org.bukkit.event.entity.EnderDragonChangePhaseEvent")) {
            Skript.registerEvent("Ender Dragon - on Phase Change", SimpleEvent.class, EnderDragonChangePhaseEvent.class, "ender dragon phase chang(e[d]|ing)")
                    .description("Called when an EnderDragon changes phase.")
                    .examples("")
                    .since("1.0.2");
        }
    }
}
