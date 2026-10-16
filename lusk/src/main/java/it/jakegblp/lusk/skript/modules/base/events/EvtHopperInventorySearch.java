package it.jakegblp.lusk.skript.modules.base.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtHopperInventorySearch extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.event(EvtHopperInventorySearch.class, HopperInventorySearchEvent.class)
                .patterns("hopper inventory search[ing]",
                        "hopper source search[ing]",
                        "hopper destination search[ing]")
                .name("Hopper Inventory Search")
                .description("Called each time a Hopper attempts to find its source/attached containers.")
                .since("1.0.4")
                .keywords("hopper", "search")
                .examples("""
                        on hopper inventory search:
                            broadcast event-inventory""")
                .register(syntaxRegistry);
        Registrations.registerEventValue(HopperInventorySearchEvent.class, Inventory.class, HopperInventorySearchEvent::getInventory);
    }

    private int pattern;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull SkriptParser.ParseResult parseResult) {
        pattern = matchedPattern;
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        var event = (HopperInventorySearchEvent) e;
        return switch (pattern) {
            case 1 -> event.getContainerType() == HopperInventorySearchEvent.ContainerType.SOURCE;
            case 2 -> event.getContainerType() == HopperInventorySearchEvent.ContainerType.DESTINATION;
            default -> true;
        };
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