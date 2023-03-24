package me.jake.lusk.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Make Trigger Advancements")
@Description("This Effect requires Paper.\n\nCan only be used in a Slot Change Event.\nSets whether or not the event should trigger advancements.")
@Examples({"""
           on item obtain:
             cancel advancements"""})
@Since("1.0.0")
public class EffCancelAdvancements extends Effect {

    static {
        Skript.registerEffect(EffCancelAdvancements.class, "cancel [the] advancements", "uncancel [the] advancements");
    }

    private boolean negated;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(PlayerInventorySlotChangeEvent.class)) {
            Skript.error("This effect can only be used in the Slot Change Event!");
            return false;
        }
        negated = matchedPattern == 1;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (negated ? "un" : "") + "cancel the advancements";
    }

    @Override
    protected void execute(@NotNull Event event) {
        ((PlayerInventorySlotChangeEvent)event).setShouldTriggerAdvancements(negated);
    }
}