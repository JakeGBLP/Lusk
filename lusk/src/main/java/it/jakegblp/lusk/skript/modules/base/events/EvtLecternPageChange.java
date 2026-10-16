package it.jakegblp.lusk.skript.modules.base.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import io.papermc.paper.event.player.PlayerLecternPageChangeEvent;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtLecternPageChange extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.event(EvtLecternPageChange.class, PlayerLecternPageChangeEvent.class)
                .patterns("lectern page flip[ping|ped] [to[wards] the] left",
                        "lectern page flip[ping|ped] [to[wards] the] right",
                        "lectern page flip[ping|ped] [(in|to[wards]) either direction|on either side]")
                .name("Lectern Page Flip")
                .description("Called when a player flips the page in a Lectern.")
                .since("1.0.4")
                .keywords("lectern", "page", "flip")
                .examples("""
                        on lectern page flip to the right:
                          broadcast "right"
                        """,
                        """
                        on lectern page flip to the left:
                        broadcast "left
                        """,
                        """
                        on lectern page flip:
                        broadcast "either
                        """)
                .register(syntaxRegistry);
        Registrations.registerEventValue(PlayerLecternPageChangeEvent.class, ItemStack.class, PlayerLecternPageChangeEvent::getBook);
        Registrations.registerEventValue(PlayerLecternPageChangeEvent.class, Block.class, e -> e.getLectern().getBlock());
    }

    private PlayerLecternPageChangeEvent.PageChangeDirection action;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull SkriptParser.ParseResult parseResult) {
        switch (matchedPattern) {
            case 0 -> action = PlayerLecternPageChangeEvent.PageChangeDirection.LEFT;
            case 1 -> action = PlayerLecternPageChangeEvent.PageChangeDirection.RIGHT;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return action == null || action == ((PlayerLecternPageChangeEvent) e).getPageChangeDirection();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "lectern page flip" + (action == null ? "" : " to the " + action.toString().toLowerCase());
    }
}