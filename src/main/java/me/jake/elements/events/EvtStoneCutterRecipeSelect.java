package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerStonecutterRecipeSelectEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EvtStoneCutterRecipeSelect extends SkriptEvent {

    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerStonecutterRecipeSelectEvent")) {
            Skript.registerEvent("StoneCutter Recipe Select", EvtStoneCutterRecipeSelect.class, PlayerStonecutterRecipeSelectEvent.class, "[stonecutt(er|ing)] recipe select")
                    .description("This Event requires Paper.\n\nCalled when a player selects a recipe in a stonecutter inventory.")
                    .examples("""
                            on stonecutter recipe select:
                              broadcast result
                            """)
                    .since("1.0.0");
        }
    }

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "stonecutter recipe select";
    }

}
