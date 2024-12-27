package it.jakegblp.lusk.elements.minecraft.blocks.composter.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.block.CompostItemEvent;
import io.papermc.paper.event.entity.EntityCompostItemEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Will Raise Composter Level")
@Description("This Condition requires Paper.\n\nChecks whether or not the composter's level will rise in a Compost Item Event")
@Examples({"""
        on hopper compost:
          if the composter level will be raised:
            cancel composter rise
        """})
@Since("1.0.0")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class CondRiseComposterLevel extends Condition {
    static {
        if (Skript.classExists("io.papermc.paper.event.block.CompostItemEvent")) {
            Skript.registerCondition(CondRiseComposterLevel.class, "[the] composter level will be raised",
                    "[the] composter level w(ill no|on')t be raised");
        }
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(CompostItemEvent.class, EntityCompostItemEvent.class))) {
            Skript.error("This condition can only be used in the Hopper Compost event!");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the composter level will " + (isNegated() ? "not" : "") + " be raised";
    }

    @Override
    public boolean check(@NotNull Event event) {
        if (event instanceof EntityCompostItemEvent entityCompostItemEvent) {
            return isNegated() ^ entityCompostItemEvent.willRaiseLevel();
        }
        return isNegated() ^ ((CompostItemEvent) event).willRaiseLevel();
    }
}