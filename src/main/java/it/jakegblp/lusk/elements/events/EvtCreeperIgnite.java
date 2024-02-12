package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtCreeperIgnite extends SkriptEvent {
    static {
        if (Skript.classExists("com.destroystokyo.paper.event.entity.CreeperIgniteEvent")) {
            Skript.registerEvent("Creeper - Ignite/Extinguish Event", EvtCreeperIgnite.class, CreeperIgniteEvent.class,
                            "creeper ignit(e[d]|ion)",
                            "creeper extinguish[ed|ing]",
                            "creeper ignite toggle[d]")
                    .description("Called when a Creeper is ignited.\nThis event requires Paper.")
                    .examples("on creeper ignite:\n\tbroadcast \"%entity% is about to explode!\"")
                    .since("1.0.2+");
        }
    }

    private Boolean ignite;

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            ignite = true;
        } else if (matchedPattern == 1) {
            ignite = false;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (ignite == null) return true;
        if (ignite) {
            return ((CreeperIgniteEvent) e).isIgnited();
        } else {
            return !((CreeperIgniteEvent) e).isIgnited();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "creeper " + (ignite == null ? "ignite toggle" : (ignite ? "ignite" : "extinguish"));
    }
}
