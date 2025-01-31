package it.jakegblp.lusk.elements.minecraft.blocks.bell.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BellResonateEvent;
import org.bukkit.event.block.BellRingEvent;

import static it.jakegblp.lusk.utils.Constants.*;
import static it.jakegblp.lusk.utils.SkriptUtils.registerEventValue;

@SuppressWarnings("unused")
public class EvtBellEvents {
    static {
        if (HAS_BELL_RESONATE_EVENT && !SKRIPT_2_9) {
            Skript.registerEvent("Bell - on Resonate", SimpleEvent.class, BellResonateEvent.class, "bell resonat(e[d]|ing)", "raider[s] reveal[ed|ing]")
                    .description("""
                            Called when a bell resonates after being rung and highlights nearby raiders.
                            A bell will only resonate if raiders are in the vicinity of the bell.""")
                    .examples("")
                    .since("1.0.2");
            registerEventValue(BellResonateEvent.class, LivingEntity[].class, e -> e.getResonatedEntities().toArray(new LivingEntity[0]), EventValues.TIME_NOW);
        }
        if (SPIGOT_HAS_BELL_RING_EVENT && !SKRIPT_2_9) {
            Skript.registerEvent("Bell - on Ring", SimpleEvent.class, BellRingEvent.class, "bell ring[ing]", "bell rung")
                    .description("`DEPRECATED SINCE SKRIPT 2.9`\nCalled when a bell is being rung.")
                    .examples("")
                    .since("1.0.2, 1.2 (Deprecated)");
            registerEventValue(BellRingEvent.class, BlockFace.class, BellRingEvent::getDirection, EventValues.TIME_NOW);
            registerEventValue(BellRingEvent.class, Entity.class, BellRingEvent::getEntity, EventValues.TIME_NOW);
        }
    }
}
