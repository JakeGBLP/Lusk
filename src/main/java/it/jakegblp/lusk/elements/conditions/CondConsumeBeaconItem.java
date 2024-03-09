package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - Will Consume Item")
@Description("Checks whether or not the beacon's item will be consumed in a Beacon Effect Change Event.")
@Examples({"""
        on beacon effect change:
          if the brewing fuel will be consumed:
            # cancels it
            cancel beacon item consume
        """})
@Since("1.0.4")
public class CondConsumeBeaconItem extends Condition {
    static {
        Skript.registerCondition(CondConsumeBeaconItem.class, "[the] beacon item will be consumed",
                "[the] beacon item w(ill not|on't) be consumed");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(PlayerChangeBeaconEffectEvent.class))) {
            Skript.error("This condition can only be used in the Beacon Effect Change event!");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the beacon item will " + (isNegated() ? "not" : "") + " be consumed";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ ((PlayerChangeBeaconEffectEvent) event).willConsumeItem();
    }
}