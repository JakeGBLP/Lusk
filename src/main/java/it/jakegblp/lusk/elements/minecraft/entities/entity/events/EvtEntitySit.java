package it.jakegblp.lusk.elements.minecraft.entities.entity.events;

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
            Skript.registerEvent("Entity - on Sit/Stand up", EvtEntitySit.class, EntityToggleSitEvent.class,
                            "[entity] s(at|it[ting]) [down]",
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
        sit = matchedPattern != 1;
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (sit == null) return true;
        return sit ^ ((EntityToggleSitEvent) e).getSittingState();

    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "entity " + (sit == null ? "sit toggle" : (sit ? "sitting down" : "standing up"));
    }
}
