package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.entity.EntityToggleSitEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtEntitySit extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.entity.EntityToggleSitEvent")) {
            Skript.registerEvent("Sit/Stand up", EvtEntitySit.class, EntityToggleSitEvent.class,
                            "[entity] sit:s(at|it[ting]) [down]",
                            "[entity] st(ood|and[ing]) [up]",
                            "[entity] sit toggle[d]")
                    .description("Called when an entity sits down or stands up.\nThis event requires Paper.")
                    .examples("""
                            on sitting down:
                            	broadcast "%entity% is taking a seat!"
                            on stand up:
                              broadcast "stand up"
                            on sit toggle:
                              broadcast "toggle"
                            """)
                    .since("1.0.0+, 1.0.2+ (Toggle)");
        }
    }

    private Boolean sit;

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            sit = true;
        } else if (matchedPattern == 1) {
            sit = false;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (sit == null) return true;
        if (sit) {
            return ((EntityToggleSitEvent) e).getSittingState();
        } else {
            return !((EntityToggleSitEvent) e).getSittingState();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "entity " + (sit == null ? "sit toggle" : (sit ? "sitting down" : "standing up"));
    }
}
