package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.papermc.paper.event.player.PlayerLecternPageChangeEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtLecternPageChange extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerLecternPageChangeEvent")) {
            Skript.registerEvent("Lectern - on Page Flip", EvtLecternPageChange.class, PlayerLecternPageChangeEvent.class,
                            "lectern page flip [to the] left",
                            "lectern page flip [to the] right",
                            "lectern page [flip]"
                    )
                    .description("This Event requires Paper.\n\nCalled when a player flips the page in a Lectern.")
                    .examples("""
                            on lectern page flip to the right:
                              broadcast "right"
                                                        
                            on lectern page flip to the left:
                              broadcast "left"
                                                        
                            on lectern page:
                              broadcast "both"
                              """)
                    .since("1.0.0");
            EventValues.registerEventValue(PlayerLecternPageChangeEvent.class, ItemStack.class, new Getter<>() {
                @Override
                public @NotNull ItemStack get(final PlayerLecternPageChangeEvent e) {
                    return e.getBook();
                }
            }, 0);
            EventValues.registerEventValue(PlayerLecternPageChangeEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(final PlayerLecternPageChangeEvent e) {
                    return e.getLectern().getBlock();
                }
            }, 0);
        }
    }

    private PlayerLecternPageChangeEvent.PageChangeDirection action;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            action = PlayerLecternPageChangeEvent.PageChangeDirection.LEFT;
        } else if (matchedPattern == 1) {
            action = PlayerLecternPageChangeEvent.PageChangeDirection.RIGHT;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (action == null) {
            return true;
        }
        return action == ((PlayerLecternPageChangeEvent) e).getPageChangeDirection();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "lectern page flip" + (action == null ? "" : " to the " + action.toString().toLowerCase());
    }
}
