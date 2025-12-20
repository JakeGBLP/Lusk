package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.util.Displayable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffShowRemoveDisplayable extends Effect {

    static {
        Skript.registerEffect(EffShowRemoveDisplayable.class,
                "(show|display) %displayables% (to|for) %players%",
                "remove %displayables% for %players%");
    }

    private Expression<Displayable> displayableExpression;
    private Expression<Player> playerExpression;
    private boolean remove;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        displayableExpression = (Expression<Displayable>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        remove = matchedPattern == 1;
        return true;
    }

    @Override // todo: null checks?
    protected void execute(Event event) {
        for (Displayable displayable : displayableExpression.getArray(event)) {
            Player[] players = playerExpression.getArray(event);
            if (remove)
                displayable.removeViewers(players);
            else
                displayable.addViewers(players);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return (remove ? "remove " : "display ") + displayableExpression.toString(event, debug) + " for " + playerExpression.toString(event, debug);
    }

}
