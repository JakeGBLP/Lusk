package it.jakegblp.lusk.elements.minecraft.blocks.bell.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import it.jakegblp.lusk.utils.Utils;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BellResonateEvent;
import org.bukkit.event.block.BellRingEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EvtBellEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.BellResonateEvent") && !Utils.SKRIPT_2_9) {
            Skript.registerEvent("Bell - on Resonate", SimpleEvent.class, BellResonateEvent.class, "bell resonat(e[d]|ing)","raider[s] reveal[ed|ing]")
                    .description("""
                            Called when a bell resonates after being rung and highlights nearby raiders.
                            A bell will only resonate if raiders are in the vicinity of the bell.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BellResonateEvent.class, LivingEntity[].class, new Getter<>() {
                @Override
                public LivingEntity @NotNull [] get(BellResonateEvent e) {
                    return e.getResonatedEntities().toArray(new LivingEntity[0]);
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.block.BellRingEvent") && !Utils.SKRIPT_2_9) {
            Skript.registerEvent("Bell - on Ring", SimpleEvent.class, BellRingEvent.class, "bell ring[ing]","bell rung")
                    .description("Called when a bell is being rung.")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BellRingEvent.class, BlockFace.class, new Getter<>() {
                @Override
                public @NotNull BlockFace get(final BellRingEvent e) {
                    return e.getDirection();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(BellRingEvent.class, Entity.class, new Getter<>() {
                @Override
                public @Nullable Entity get(final BellRingEvent e) {
                    return e.getEntity();
                }
            }, EventValues.TIME_NOW);
        }
    }
}
