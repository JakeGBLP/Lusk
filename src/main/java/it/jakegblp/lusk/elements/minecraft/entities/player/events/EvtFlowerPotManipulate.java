package it.jakegblp.lusk.elements.minecraft.entities.player.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.DeprecationUtils.registerEventValue;

@SuppressWarnings("unused")
public class EvtFlowerPotManipulate extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent")) {
            Skript.registerEvent("Flower Pot - on Manipulate", EvtFlowerPotManipulate.class, PlayerFlowerPotManipulateEvent.class,
                            "[flower] pot[ting)| manipulat(e|ing)] [of %itemtype%]"
                    )
                    .description("Called when a player places an item in or takes an item out of a flowerpot.")
                    .examples("""
                            on potting of cornflower:
                              if the plant is being placed:
                                broadcast "placed"
                              else if the plant is being picked up:
                                broadcast "picked up"
                            """)
                    .requiredPlugins("Paper")
                    .since("1.0.0");
            registerEventValue(PlayerFlowerPotManipulateEvent.class, ItemStack.class, PlayerFlowerPotManipulateEvent::getItem, 0);
            registerEventValue(PlayerFlowerPotManipulateEvent.class, Block.class, PlayerFlowerPotManipulateEvent::getFlowerpot, 0);
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
        return "flower pot" + (flower == null ? "" : " of " + flower);
    }
}
