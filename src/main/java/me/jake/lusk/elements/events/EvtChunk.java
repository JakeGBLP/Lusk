package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@SuppressWarnings("unused")
public class EvtChunk extends SkriptEvent {

    static {
        if (!Skript.classExists("ch.njol.skript.events.bukkit.EventPlayerEnterChunk")) {
            Skript.registerEvent("Chunk Enter Event", EvtChunk.class, PlayerMoveEvent.class, "chunk enter")
                    .description("Called when a player enters a chunk.")
                    .examples("on chunk enter:\n\tif {adminChunks::*} contains event-chunk:\n\t\tkill player")
                    .since("1.0.0");
        }
    }

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        Location from = ((PlayerMoveEvent)e).getFrom();
        Location to = ((PlayerMoveEvent)e).getTo();
        return from.getChunk() != to.getChunk();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "chunk enter";
    }

}