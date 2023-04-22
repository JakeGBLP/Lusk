package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtBeaconEffectApply extends SkriptEvent {
    static {
        if (Skript.classExists("com.destroystokyo.paper.event.block.BeaconEffectEvent")) {
            Skript.registerEvent("Beacon - Effect Applied Event", EvtBeaconEffectApply.class, BeaconEffectEvent.class, "beacon effect [appl(ied|y)]")
                    .description("This Event requires Paper.\n\nCalled when a beacon effect is being applied to a player.")
                    .examples("on beacon effect:\n\tbroadcast \"A beacon has applied its effect!\"")
                    .since("1.0.3");
        }
    }

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "beacon effect applied";
    }

}
