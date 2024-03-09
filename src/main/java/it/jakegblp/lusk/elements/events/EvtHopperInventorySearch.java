package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtHopperInventorySearch extends SkriptEvent {
    static {
        if (Skript.classExists("org.bukkit.event.inventory.HopperInventorySearchEvent")) {
            Skript.registerEvent("Hopper - on Inventory Search", EvtHopperInventorySearch.class, HopperInventorySearchEvent.class,
                            "hopper inventory search",
                            "hopper source search",
                            "hopper destination search")
                    .description("Called each time a Hopper attempts to find its source/attached containers.")
                    .examples("on hopper inventory search:\n\tbroadcast event-inventory")
                    .since("1.0.4");
            EventValues.registerEventValue(HopperInventorySearchEvent.class, Inventory.class, new Getter<>() {
                @Override
                public @javax.annotation.Nullable Inventory get(final HopperInventorySearchEvent e) {
                    return e.getInventory();
                }
            }, 0);
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
