package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EvtInventorySlotChange extends SkriptEvent {

    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerInventorySlotChangeEvent")) {
            Skript.registerEvent("Player Inventory Slot Change", EvtInventorySlotChange.class, PlayerInventorySlotChangeEvent.class,
                            "[player] [inventory] slot change", "[player] item (obtain|get)", "[player] item lose"
                    )
                    .description("This Event requires Paper.\n\nCalled when a slot contents change in a player's inventory.")
                    .examples("""
                            on slot change:
                              broadcast past item and item and event-slot
                            
                            on item obtain:
                              if item is a sword:
                                set lore of event-slot to "&fThis is a sword."
                            """)
                    .since("1.0.0");
        }
    }

    private Boolean obtain = null;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (matchedPattern == 1) {
            obtain = true;
        } else if (matchedPattern == 2) {
            obtain = false;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (obtain == null) {
            return true;
        }
        if (!obtain) {
            return ((PlayerInventorySlotChangeEvent) e).getNewItemStack().getType().isAir();
        } else {
            return !((PlayerInventorySlotChangeEvent) e).getNewItemStack().getType().isAir();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "Player Inventory Slot Change";
    }

}
