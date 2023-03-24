package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import org.bukkit.event.Event;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtBatToggleSleep extends SkriptEvent {

    static {
        Skript.registerEvent("Bat Sleep/Wake up", EvtBatToggleSleep.class, BatToggleSleepEvent.class, "bat (:sleep|wake up)")
                .description("Called when a bat attempts to sleep or wake up from its slumber.")
                .examples("on wake up:\n\tbroadcast \"A bat has woken up!\"")
                .since("1.0.0");
    }

    private boolean sleeping;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, ParseResult parseResult) {
        sleeping = parseResult.hasTag("sleep");
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (sleeping) {
            return !((BatToggleSleepEvent) e).isAwake();
        } else {
            return ((BatToggleSleepEvent) e).isAwake();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "bat " + (sleeping ? "sleep" : "wake up");
    }

}
