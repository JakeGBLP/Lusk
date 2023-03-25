package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtItemFrameChange extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerItemFrameChangeEvent")) {
            Skript.registerEvent("Item Frame Change", EvtItemFrameChange.class, PlayerItemFrameChangeEvent.class,
                            "item[ ]frame [place:(insert|place)|:remove|:rotate|interact|change]"
                    )
                    .description("This Event requires Paper.\n\nCalled when an ItemFrame is having an item rotated, added, or removed from it.")
                    .examples("""
                            on itemframe rotate:
                              broadcast "rotated"

                            on itemframe remove:
                              broadcast "remove"

                            on itemframe place:
                              broadcast "place"
                              """)
                    .since("1.0.0");
        }
    }

    private PlayerItemFrameChangeEvent.ItemFrameChangeAction action;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (parseResult.hasTag("place")) {
            action = PlayerItemFrameChangeEvent.ItemFrameChangeAction.PLACE;
        } else if (parseResult.hasTag("remove")) {
            action = PlayerItemFrameChangeEvent.ItemFrameChangeAction.REMOVE;
        } else if (parseResult.hasTag("rotate")) {
            action = PlayerItemFrameChangeEvent.ItemFrameChangeAction.ROTATE;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (action == null) {
            return true;
        }
        return action == ((PlayerItemFrameChangeEvent)e).getAction();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return switch (action) {
            case PLACE -> "item frame place";
            case REMOVE -> "item frame remove";
            case ROTATE -> "item frame rotate";
            default -> "item frame interact";
        };
    }
}
