package it.jakegblp.lusk.elements.minecraft.blocks.composter.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.block.CompostItemEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Cancel Composter Level Rise")
@Description("This Effect requires Paper.\n\nCan only be used in a Compost Item Event.\nSets whether or not the composter's level should rise.")
@Examples({"""
        on hopper compost:
          cancel composter level rise"""})
@Since("1.0.0")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class EffCancelComposterRising extends Effect {
    // todo: fuse all cancel effects?
    static {
        Skript.registerEffect(EffCancelComposterRising.class, "[:un]cancel [the] composter [level] rise");
    }

    private boolean uncancel;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(CompostItemEvent.class)) {
            Skript.error("This effect can only be used in the Compost Item Event!");
            return false;
        }
        uncancel = parser.hasTag("un");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (uncancel ? "un" : "") + "cancel the composter level raise";
    }

    @Override
    protected void execute(@NotNull Event event) {
        ((CompostItemEvent) event).setWillRaiseLevel(uncancel);
    }
}