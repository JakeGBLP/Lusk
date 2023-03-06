package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.entity.EntityToggleSitEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EvtEntitySit extends SkriptEvent {

    static {
        if (Skript.classExists("io.papermc.paper.event.entity.EntityToggleSitEvent")) {
            Skript.registerEvent("Sit/Stand up", EvtEntitySit.class, EntityToggleSitEvent.class, "[entity] (sit:s(at|it[ting]) [down]|st(ood|and[ing]) [up]) [toggle]")
                    .description("Is called when an entity sits down or stands up..\n\nThis event requires Paper.")
                    .examples("on sitting down:\n\tbroadcast \"%entity% is taking a seat!\"")
                    .since("1.0.0");
        }
    }

    private boolean sit;

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        sit = parseResult.hasTag("sit");
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (sit) {
            return ((EntityToggleSitEvent) e).getSittingState();
        } else {
            return !((EntityToggleSitEvent) e).getSittingState();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "Sit/Stand";
    }

}
