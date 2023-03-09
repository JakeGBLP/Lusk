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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;

@Name("Show Credits")
@Description("Shows the player the win screen that normally is only displayed after one kills the ender dragon and exits the end for the first time.")
@Examples({"""
           show the credits to all players"""})
@Since("1.0.0")
public class EffShowCredits extends Effect {

    static {
        Skript.registerEffect(EffShowCredits.class, "show [the] (credits|end poem) to %players%");
    }

    private Expression<Player> players;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        players = (Expression<Player>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "show the end poem to " + (event == null ? "" : Arrays.toString(players.getArray(event)));
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (Player player : players.getArray(event)) {
            player.showWinScreen();
        }
    }
}