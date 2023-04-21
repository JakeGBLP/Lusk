package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Has Seen Credits")
@Description("Checks if a player has seen the end credits.")
@Examples({"if target has seen the credits:"})
@Since("1.0.2")
public class CondHasSeenCredits extends Condition {
    static {
        Skript.registerCondition(CondHasSeenCredits.class, "%player% has seen [the] ((win|victory) screen|end (credits|poem)|credits)",
                "%player% has(n't| not) seen [the] ((win|victory) screen|end (credits|poem)|credits)");
    }

    private Expression<Player> playerExpression;
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        playerExpression = (Expression<Player>) expressions[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event == null ? "" : playerExpression.getSingle(event)) + " has" + (isNegated() ? "n't" : "") + " seen the end poem";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Player player = playerExpression.getSingle(event);
        if (player != null) {
            return isNegated() ^ player.hasSeenWinScreen();
        }
        return false;
    }
}