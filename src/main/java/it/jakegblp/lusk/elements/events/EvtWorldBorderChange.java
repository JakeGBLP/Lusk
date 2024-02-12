package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.coll.CollectionUtils;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeFinishEvent;
import io.papermc.paper.event.world.border.WorldBorderCenterChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtWorldBorderChange extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent")) {
            Skript.registerEvent("WorldBorder - Change/Center Change Event", EvtWorldBorderChange.class, CollectionUtils.array(WorldBorderBoundsChangeEvent.class, WorldBorderBoundsChangeFinishEvent.class, WorldBorderCenterChangeEvent.class),
                            "world[ ]border start chang(e|ing)",
                            "world[ ]border stop chang(e|ing)",
                            "world[ ]border center change"
                    )
                    .description("This Event requires Paper.")
                    .examples("""
                            on world border start changing:
                              if the world border change is instant:
                                broadcast "instant"
                              else:
                                broadcast "not instant"
                            """)
                    .since("1.0.2");
        }
    }

    private int pattern;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        pattern = matchedPattern;
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (e instanceof WorldBorderBoundsChangeEvent) {
            return pattern == 0;
        } else if (e instanceof WorldBorderBoundsChangeFinishEvent) {
            return pattern == 1;
        } else {
            return pattern == 2;
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return pattern == 0 ? "worldborder start changing" : (pattern == 1 ? "worldborder stop changing" : "worldborder center change");
    }
}
