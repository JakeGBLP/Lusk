package it.jakegblp.lusk.elements.minecraft.blocks.dispenser.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.jetbrains.annotations.NotNull;

public class EvtDispenserEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.BlockDispenseArmorEvent")) {
            Skript.registerEvent("Dispenser - on Armor Dispense", SimpleEvent.class, BlockDispenseArmorEvent.class, "armor dispens(e[d]|ing)")
                    .description("Called when an equippable item is dispensed from a block and equipped on a nearby entity.")
                    .examples("")
                    .since("1.0.0");
            EventValues.registerEventValue(BlockDispenseArmorEvent.class, Entity.class, new Getter<>() {
                @Override
                public Entity get(final BlockDispenseArmorEvent e) {
                    return e.getTargetEntity();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockFailedDispenseEvent")) {
            Skript.registerEvent("Dispenser - on Dispense Fail", SimpleEvent.class, BlockFailedDispenseEvent.class, "dispense fail", "failed [to] dispense")
                    .description("""
                            This event requires Paper.
                            
                            Called when a block tries to dispense an item, but its inventory is empty.""")
                    .examples("")
                    .since("1.0.2");
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockPreDispenseEvent")) {
            Skript.registerEvent("Dispenser - on Pre Dispense", SimpleEvent.class, BlockPreDispenseEvent.class, "pre[-| ]dispens(e[d]|ing)")
                    .description("""
                            This event requires Paper.
                            
                            Called when a block is about to dispense an item.""")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BlockPreDispenseEvent.class, Integer.class, new Getter<>() {
                @Override
                public @NotNull Integer get(final BlockPreDispenseEvent e) {
                    return e.getSlot();
                }
            }, EventValues.TIME_NOW);
        }
    }
}
