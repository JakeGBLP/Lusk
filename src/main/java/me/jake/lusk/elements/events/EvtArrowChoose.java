package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtArrowChoose extends SkriptEvent {

    static {
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerReadyArrowEvent")) {
            Skript.registerEvent("Arrow Choose", EvtArrowChoose.class, PlayerReadyArrowEvent.class, "arrow (ready|choose)")
                    .description("This Event requires Paper.\n\nCalled when a player is firing a bow/crossbow and the server is choosing an arrow to use.")
                    .examples("on arrow ready:\n\tbroadcast the arrow and the bow")
                    .since("1.0.0");
        }
    }

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "arrow choose";
    }

}