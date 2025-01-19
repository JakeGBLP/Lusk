package it.jakegblp.lusk.elements.minecraft.blocks.dispenser.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockDispenseArmorEvent;

import static it.jakegblp.lusk.utils.DeprecationUtils.registerEventValue;

public class EvtDispenserEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.BlockDispenseArmorEvent")) {
            Skript.registerEvent("Dispenser - on Armor Dispense", SimpleEvent.class, BlockDispenseArmorEvent.class, "armor dispens(e[d]|ing)")
                    .description("Called when an equipable item is dispensed from a block and equipped on a nearby entity.")
                    .examples("")
                    .since("1.0.0");
            registerEventValue(BlockDispenseArmorEvent.class, Entity.class, BlockDispenseArmorEvent::getTargetEntity, EventValues.TIME_NOW);
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockFailedDispenseEvent")) {
            Skript.registerEvent("Dispenser - on Dispense Fail", SimpleEvent.class, BlockFailedDispenseEvent.class, "dispense fail", "failed [to] dispense")
                    .description("""
                            Called when a block tries to dispense an item, but its inventory is empty.""")
                    .examples("")
                    .requiredPlugins("Paper")
                    .since("1.0.2");
        }
        if (Skript.classExists("io.papermc.paper.event.block.BlockPreDispenseEvent")) {
            Skript.registerEvent("Dispenser - on Pre Dispense", SimpleEvent.class, BlockPreDispenseEvent.class, "pre[-| ]dispens(e[d]|ing)")
                    .description("""
                            Called when a block is about to dispense an item.""")
                    .examples("")
                    .requiredPlugins("Paper")
                    .since("1.0.2");
            registerEventValue(BlockPreDispenseEvent.class, Integer.class, BlockPreDispenseEvent::getSlot, EventValues.TIME_NOW);
        }
    }
}
