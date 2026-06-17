package it.jakegblp.lusk.skript.modules.packets.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.util.Displayable;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffShowRemoveDisplayable extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerEffect(syntaxRegistry, EffShowRemoveDisplayable.class, EffShowRemoveDisplayable::new,
                "display %displayables% (to|for) %players%",
                "(conceal|undisplay) %displayables% for %players%");
    }

    private Expression<Displayable> displayableExpression;
    private Expression<Player> playerExpression;
    private boolean undisplay;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        displayableExpression = (Expression<Displayable>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        undisplay = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(Event event) {
        for (Displayable displayable : displayableExpression.getArray(event)) {
            Player[] players = playerExpression.getArray(event);
            if (undisplay)
                displayable.removeViewers(players);
            else
                displayable.addViewers(players);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return (undisplay ? "undisplay " : "display ") + displayableExpression.toString(event, debug) + " for " + playerExpression.toString(event, debug);
    }

}
