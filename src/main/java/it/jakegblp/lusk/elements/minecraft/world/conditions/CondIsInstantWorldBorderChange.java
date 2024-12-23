package it.jakegblp.lusk.elements.minecraft.world.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("WorldBorder - Change is Instant")
@Description("This Condition requires Paper.\n\nChecks whether or not the worldborder change is instant in the worldborder change event")
@Examples({"""
        on world border start changing:
          if the world border change is instant:
            broadcast "instant"
          else:
            broadcast "not instant"
        """})
@Since("1.0.2")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class CondIsInstantWorldBorderChange extends Condition {
    static {
        if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent")) {
            Skript.registerCondition(CondIsInstantWorldBorderChange.class, "[the] world[ ]border change is[not:n('|o)t] instant");
        }
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(WorldBorderBoundsChangeEvent.class))) {
            Skript.error("This condition can only be used in the WorldBorder Change event!");
            return false;
        }
        setNegated(parser.hasTag("not"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the worldborder change is " + (isNegated() ? "not " : "") + "instant";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return isNegated() ^ ((WorldBorderBoundsChangeEvent) event).getType() == WorldBorderBoundsChangeEvent.Type.INSTANT_MOVE;
    }
}