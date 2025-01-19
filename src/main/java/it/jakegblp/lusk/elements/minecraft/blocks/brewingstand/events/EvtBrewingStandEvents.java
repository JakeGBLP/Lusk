package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import it.jakegblp.lusk.api.BlockWrapper;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.Inventory;

import static it.jakegblp.lusk.utils.DeprecationUtils.registerEventValue;

public class EvtBrewingStandEvents {
    static {
        if (Skript.classExists("org.bukkit.event.block.BrewingStartEvent")) {
            Skript.registerEvent("Brewing Stand - on Brewing Start", SimpleEvent.class, BrewingStartEvent.class, "brewing [stand] (start[ing]|begin[ning])")
                    .description(" Called when a brewing stand starts to brew.")
                    .examples("")
                    .since("1.0.2");
            registerEventValue(BrewingStartEvent.class, Inventory.class, e -> new BlockWrapper(e.getBlock()).getBrewerInventory(), EventValues.TIME_NOW);
            registerEventValue(BrewingStartEvent.class, ItemType.class, e -> new BlockWrapper(e.getBlock()).getBrewingIngredient(), EventValues.TIME_NOW);
        }
        Skript.registerEvent("Brewing Stand - on Brew", SimpleEvent.class, BrewEvent.class, "[brewing stand] brew[ing]")
                .description("Called when the brewing of the contents inside a Brewing Stand is complete.")
                .examples("")
                .since("1.0.2");
        registerEventValue(BrewEvent.class, Inventory.class, BrewEvent::getContents, EventValues.TIME_NOW);
        Skript.registerEvent("Brewing Stand - on Fuel", SimpleEvent.class, BrewingStandFuelEvent.class, "brewing [stand] fuel [consume]")
                .description("Called when an ItemStack is about to increase the fuel level of a brewing stand.")
                .examples("")
                .since("1.0.2");
    }
}
