package it.jakegblp.lusk.elements.minecraft.entities.bat.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.LuskUtils;
import org.bukkit.event.Event;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtBatToggleSleep extends SkriptEvent {
    static {
        Skript.registerEvent("Bat - on Sleep/Wake up", EvtBatToggleSleep.class, BatToggleSleepEvent.class,
                        "bat sleep[ing]",
                        "bat wak(e|ing) up",
                        "bat sleep[ing] toggle[d]")
                .description("Called when a bat attempts to sleep or wake up from its slumber.")
                .examples("on bat wake up:\n\tbroadcast \"A bat has woken up!\"")
                .since("1.0.0, 1.0.2 (Toggle)");
    }

    private Kleenean sleep;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        sleep = LuskUtils.getKleenean(matchedPattern == 0, matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return switch (sleep) {
            case UNKNOWN -> false;
            case FALSE -> ((BatToggleSleepEvent) e).isAwake();
            case TRUE -> !((BatToggleSleepEvent) e).isAwake();
        };
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "bat " + switch (sleep) {
            case UNKNOWN -> "sleep toggle";
            case FALSE -> "wake up";
            case TRUE -> "sleep";
        };
    }

}
