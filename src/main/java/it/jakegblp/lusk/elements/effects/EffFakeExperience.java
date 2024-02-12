package it.jakegblp.lusk.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;

@Name("Fake Experience")
@Description("Send an experience change. This fakes an experience change packet for a user. This will not actually change the experience points in any way.\nProgress must be within 0 and 100.")
@Examples({"""
        show fake xp level 69 and progress 50% to all players"""})
@Since("1.0.2")
public class EffFakeExperience extends Effect {
    static {
        Skript.registerEffect(EffFakeExperience.class, "show [fake] [[e]xp[erience]] level %integer% and progress %number%\\% to %players%",
                "show [fake] [[e]xp[erience]] level %integer% to %players%",
                "show [fake] [[e]xp[erience]] progress %number%\\% to %players%");
    }

    private Expression<Integer> level;
    private Expression<Number> progress;
    private Expression<Player> players;
    private int pattern;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        pattern = matchedPattern;
        if (pattern == 2) {
            progress = (Expression<Number>) expressions[0];
            players = (Expression<Player>) expressions[1];
        } else if (pattern == 1) {
            level = (Expression<Integer>) expressions[0];
            players = (Expression<Player>) expressions[1];
        } else {
            level = (Expression<Integer>) expressions[0];
            progress = (Expression<Number>) expressions[1];
            players = (Expression<Player>) expressions[2];
        }
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        if (event != null) {
            Integer lvl = level.getSingle(event);
            Number xp = progress.getSingle(event);
            Player[] playerArray = players.getArray(event);
            if (pattern == 2) {
                return "show fake experience progress " + xp + "% to " + Arrays.toString(playerArray);
            } else if (pattern == 1) {
                return "show fake experience level " + lvl + " to " + Arrays.toString(playerArray);
            } else {
                return "show fake experience level " + lvl + " and progress " + xp + "% to " + Arrays.toString(playerArray);
            }
        } else if (pattern == 2) {
            return "show fake experience progress  % to  ";
        } else if (pattern == 1) {
            return "show fake experience level   to  ";
        } else {
            return "show fake experience level   and progress  % to  ";
        }
    }

    @Override
    protected void execute(@NotNull Event event) {
        Number xp = null;
        Integer lvl = null;
        if (pattern == 0 || pattern == 1) {
            lvl = level.getSingle(event);
        }
        if (pattern == 0 || pattern == 2) {
            xp = progress.getSingle(event);
        }
        for (Player player : players.getArray(event)) {
            if (pattern == 2) {
                if (xp == null) return;
                player.sendExperienceChange(xp.floatValue() / 100);
            } else if (pattern == 1) {
                if (lvl == null) return;
                int calcNext = Utils.getTotalNeededXP(player.getLevel() + 1);
                int calcCurrent = Utils.getTotalNeededXP(player.getLevel());
                int calcFull = calcNext - calcCurrent;
                xp = (player.getTotalExperience() - calcCurrent) / calcFull;
                player.sendExperienceChange(xp.floatValue() / 100, lvl);
            } else {
                if (lvl == null || xp == null) return;
                player.sendExperienceChange(xp.floatValue() / 100, lvl);
            }
        }
    }
}