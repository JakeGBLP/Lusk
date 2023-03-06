package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import io.papermc.paper.event.player.PlayerLecternPageChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EvtLecternPageChange extends SkriptEvent {

    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerLecternPageChangeEvent")) {
            Skript.registerEvent("Item Frame Change", EvtLecternPageChange.class, PlayerLecternPageChangeEvent.class,
                            "lectern page [flip [left:[to the] left|right:[to the] right]]"
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
        }
    }

    private PlayerLecternPageChangeEvent.PageChangeDirection action;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (parseResult.hasTag("left")) {
            action = PlayerLecternPageChangeEvent.PageChangeDirection.LEFT;
        } else if (parseResult.hasTag("right")) {
            action = PlayerLecternPageChangeEvent.PageChangeDirection.RIGHT;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (action == null) {
            return true;
        }
        return action == ((PlayerLecternPageChangeEvent)e).getPageChangeDirection();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "Flower Pot Manipulate";
    }

}
