package it.jakegblp.lusk.elements.minecraft.blocks.block.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

public class EvtBlockEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.FluidLevelChangeEvent")) {
            Skript.registerEvent("Block - on Fluid Level Change", SimpleEvent.class, FluidLevelChangeEvent.class, "fluid level chang(e[d]|ing)")
                    .description("""
                            Called when the fluid level of a block changes due to changes in adjacent blocks.""")
                    .examples("")
                    .since("1.0.4");
            EventValues.registerEventValue(FluidLevelChangeEvent.class, BlockData.class, new Getter<>() {
                @Override
                public @NotNull BlockData get(final FluidLevelChangeEvent e) {
                    return e.getNewData();
                }
            }, EventValues.TIME_NOW);
        }
    }
}
