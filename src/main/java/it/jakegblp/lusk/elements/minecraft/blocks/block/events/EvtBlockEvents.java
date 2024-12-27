package it.jakegblp.lusk.elements.minecraft.blocks.block.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.papermc.paper.event.block.BlockBreakProgressUpdateEvent;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_4;

public class EvtBlockEvents {
    static {
        Skript.registerEvent("Block - on Fluid Level Change", SimpleEvent.class, FluidLevelChangeEvent.class, "fluid level chang(e[d]|ing)")
                .description("""
                        Called when the fluid level of a block changes due to changes in adjacent blocks.""")
                .examples("on fluid level change:")
                .since("1.0.4");
        EventValues.registerEventValue(FluidLevelChangeEvent.class, BlockData.class, new Getter<>() {
            @Override
            public @NotNull BlockData get(final FluidLevelChangeEvent e) {
                return e.getNewData();
            }
        }, EventValues.TIME_NOW);
        if (MINECRAFT_1_20_4 && isPaper()) {
            Skript.registerEvent("Block - on Damage Update", SimpleEvent.class, BlockBreakProgressUpdateEvent.class,
                    "block damag(ing|e) update", "block break progress update")
                    .description("""
                            Called when the progress of a block break is updated.
                            `event-number` = the block damage progress, ranges from 0.0 to 1.0, where 0 is no damage and 1.0 is the most damage.""")
                    .examples("on block damage update:")
                    .since("1.3");
            EventValues.registerEventValue(BlockBreakProgressUpdateEvent.class, Number.class, new Getter<>() {
                @Override
                public @NotNull Number get(final BlockBreakProgressUpdateEvent e) {
                    return e.getProgress();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(BlockBreakProgressUpdateEvent.class, Entity.class, new Getter<>() {
                @Override
                public @NotNull Entity get(final BlockBreakProgressUpdateEvent e) {
                    return e.getEntity();
                }
            }, EventValues.TIME_NOW);

        }
    }
}
