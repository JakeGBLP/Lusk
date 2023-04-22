package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerLoomPatternSelectEvent;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@SuppressWarnings("unused")
public class EvtLoomPatternSelect extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerLoomPatternSelectEvent")) {
            Skript.registerEvent("Loom - Pattern Select Event", EvtLoomPatternSelect.class, PlayerLoomPatternSelectEvent.class, "[loom] pattern select [of %-patterntypes%]")
                    .description("This Event requires Paper.\n\nCalled when a player selects a banner pattern in a loom inventory.")
                    .examples("""
                            on pattern select of border:
                              set pattern to creeper

                            on pattern select:
                              broadcast event-patterntype

                            on pattern select of bricks:
                              broadcast the selected pattern
                              """)
                    .since("1.0.0");
        }
    }

    @Nullable
    private PatternType[] types = new PatternType[0];

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (args.length > 0) {
            types = args[0] == null ? null : (PatternType[]) (args[0]).getAll();
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (types == null) {
            return true;
        }
        final @NotNull PatternType patternType = ((PlayerLoomPatternSelectEvent) e).getPatternType();
        for (final PatternType type : types) {
            if (type != null) {
                if (type == patternType) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "loom pattern select" + (types == null ? "" : " " + Arrays.toString(types));
    }
}
