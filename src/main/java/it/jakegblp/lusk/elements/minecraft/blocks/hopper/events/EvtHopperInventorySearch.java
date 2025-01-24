package it.jakegblp.lusk.elements.minecraft.blocks.hopper.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.HAS_HOPPER_INVENTORY_SEARCH_EVENT;
import static it.jakegblp.lusk.utils.CompatibilityUtils.registerEventValue;

@SuppressWarnings("unused")
public class EvtHopperInventorySearch extends SkriptEvent {
    static {
        if (HAS_HOPPER_INVENTORY_SEARCH_EVENT) {
            Skript.registerEvent("Hopper - on Inventory Search", EvtHopperInventorySearch.class, HopperInventorySearchEvent.class,
                            "hopper inventory search[ing]",
                            "hopper source search[ing]",
                            "hopper destination search[ing]")
                    .description("Called each time a Hopper attempts to find its source/attached containers.")
                    .examples("on hopper inventory search:\n\tbroadcast event-inventory")
                    .since("1.0.4");
            registerEventValue(HopperInventorySearchEvent.class, Inventory.class, HopperInventorySearchEvent::getInventory, 0);
        }
    }

    private int pattern;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        pattern = matchedPattern;
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (pattern == 1)
            return ((HopperInventorySearchEvent) e).getContainerType() == HopperInventorySearchEvent.ContainerType.SOURCE;
        if (pattern == 2)
            return ((HopperInventorySearchEvent) e).getContainerType() == HopperInventorySearchEvent.ContainerType.DESTINATION;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "hopper " + (switch (pattern) {
            case 1 -> "source";
            case 2 -> "destination";
            default -> "inventory";
        } + " search");
    }
}
