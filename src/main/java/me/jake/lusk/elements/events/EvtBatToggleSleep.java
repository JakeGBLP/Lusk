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
        Skript.registerEvent("Bat - Sleep/Wake up Event", EvtBatToggleSleep.class, BatToggleSleepEvent.class, "bat sleep","bat wake up","bat sleep toggle")
                .description("Called when a bat attempts to sleep or wake up from its slumber.")
                .examples("on bat wake up:\n\tbroadcast \"A bat has woken up!\"")
                .since("1.0.0+, 1.0.2+ (Toggle)");
    }

    private Boolean sleep;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            sleep = true;
        } else if (matchedPattern == 1) {
            sleep = false;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (sleep == null) return true;
        if (sleep) {
            return !((BatToggleSleepEvent) e).isAwake();
        } else {
            return ((BatToggleSleepEvent) e).isAwake();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "bat " + (sleep == null ? "sleep toggle" : (sleep ? "sleep" : "wake up"));
    }

}
