package it.jakegblp.lusk.elements.minecraft.blocks.lectern.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerLecternPageChangeEvent;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.SkriptUtils.registerEventValue;

@SuppressWarnings("unused")
public class EvtLecternPageChange extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerLecternPageChangeEvent")) {
            Skript.registerEvent("Lectern - on Page Flip", EvtLecternPageChange.class, PlayerLecternPageChangeEvent.class,
                            "lectern page flip[ping|ped] [to[wards] the] left",
                            "lectern page flip[ping|ped] [to[wards] the] right",
                            "lectern page flip[ping|ped] [(in|to[wards]) either direction|on either side]"
                    )
                    .description("Called when a player flips the page in a Lectern.")
                    .examples("""
                            on lectern page flip to the right:
                              broadcast "right"

                            on lectern page flip to the left:
                              broadcast "left"

                            on lectern page flip:
                              broadcast "either"
                            """)
                    .since("1.0.0")
                    .requiredPlugins("Paper");
            registerEventValue(PlayerLecternPageChangeEvent.class, ItemStack.class, PlayerLecternPageChangeEvent::getBook, 0);
            registerEventValue(PlayerLecternPageChangeEvent.class, Block.class, e -> e.getLectern().getBlock(), 0);
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
