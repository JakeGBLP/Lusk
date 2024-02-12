package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtAllayDuplicate extends SkriptEvent {
    static {
        Skript.registerEvent("Allay - Duplicate Event", EvtAllayDuplicate.class, CreatureSpawnEvent.class, "allay duplicat(e[d]|ing|ion)")
                .description("Called when an allay duplicates itself.")
                .examples("on allay duplication:\n\tbroadcast event-entity")
                .since("1.0.2+");
    }

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        CreatureSpawnEvent event = (CreatureSpawnEvent) e;
        return event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DUPLICATION;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "allay duplicate";
    }

}
