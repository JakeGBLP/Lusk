package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EvtBrewingStandEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.BrewingStartEvent")) {
            Skript.registerEvent("Brewing Stand - on Brewing Start", SimpleEvent.class, BrewingStartEvent.class, "brewing [stand] (start[ing]|begin[ning])")
                    .description(" Called when a brewing stand starts to brew.")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BrewingStartEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @NotNull Inventory get(final BrewingStartEvent e) {
                    return ((InventoryHolder) e.getBlock()).getInventory();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(BrewingStartEvent.class, ItemType.class, new Getter<>() {
                @Override
                public @Nullable ItemType get(final BrewingStartEvent e) {
                    ItemStack itemStack = ((BrewingStand) e.getBlock()).getInventory().getIngredient();
                    if (itemStack != null) {
                        return new ItemType(itemStack);
                    }
                    return null;
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.inventory.BrewEvent")) {
            Skript.registerEvent("Brewing Stand - on Brew", SimpleEvent.class, BrewEvent.class, "[brewing stand] brew[ing]")
                    .description("Called when the brewing of the contents inside a Brewing Stand is complete.")
                    .examples("")
                    .since("1.0.2");
            EventValues.registerEventValue(BrewEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @NotNull Inventory get(final BrewEvent e) {
                    return e.getContents();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.inventory.BrewingStandFuelEvent")) {
            Skript.registerEvent("Brewing Stand - on Fuel", SimpleEvent.class, BrewingStandFuelEvent.class, "brewing [stand] fuel [consume]")
                    .description("Called when an ItemStack is about to increase the fuel level of a brewing stand.")
                    .examples("")
                    .since("1.0.2");
        }
    }
}
