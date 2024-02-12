package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtFlowerPot extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent")) {
            Skript.registerEvent("Flower Pot - Manipulate Event", EvtFlowerPot.class, PlayerFlowerPotManipulateEvent.class,
                            "[flower] pot[t(ed|ing)] [of %itemtype%]"
                    )
                    .description("Called when a player places an item in or takes an item out of a flowerpot.")
                    .examples("""
                            on potting of cornflower:
                              if the plant is being placed:
                                broadcast "placed"
                              else if the plant is being picked up:
                                broadcast "picked up"
                                """)
                    .since("1.0.0");
        }
    }

    private ItemType flower;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (args.length > 0) {
            flower = args[0] == null ? null : (ItemType) (args[0]).getSingle();
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (flower == null) {
            return true;
        }
        final @NotNull Material item = ((PlayerFlowerPotManipulateEvent) e).getItem().getType();
        return flower.getMaterial() == item;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "flower potting" + (flower == null ? "" : " " + flower);
    }
}
