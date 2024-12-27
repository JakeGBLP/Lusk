package it.jakegblp.lusk.elements.minecraft.entities.creeper.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import it.jakegblp.lusk.utils.LuskUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtCreeperIgnite extends SkriptEvent {
    static {
        if (Skript.classExists("com.destroystokyo.paper.event.entity.CreeperIgniteEvent")) {
            Skript.registerEvent("Creeper - on Ignite/Extinguish", EvtCreeperIgnite.class, CreeperIgniteEvent.class,
                            "creeper ignit(e[d]|ion|ing)",
                            "creeper extinguish[ed|ing]",
                            "creeper ignite toggle[d]")
                    .description("Called when a Creeper is ignited.")
                    .examples("on creeper ignite:\n\tbroadcast \"%entity% is about to explode!\"")
                    .since("1.0.2+")
                    .requiredPlugins("Paper");
        }
    }

    private Kleenean ignite;

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        ignite = LuskUtils.getKleenean(matchedPattern == 0, matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return switch (ignite) {
            case UNKNOWN -> false;
            case FALSE -> !((CreeperIgniteEvent) e).isIgnited();
            case TRUE -> ((CreeperIgniteEvent) e).isIgnited();
        };
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "creeper "  + switch (ignite) {
            case UNKNOWN -> "ignite toggle";
            case FALSE -> "extinguish";
            case TRUE -> "ignite";
        };
    }
}
