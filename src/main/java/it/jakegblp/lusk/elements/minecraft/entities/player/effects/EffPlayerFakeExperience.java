package it.jakegblp.lusk.elements.minecraft.entities.player.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.LuskUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Player - Fake Experience")
@Description("Send an experience change. This fakes an experience change packet for a user. This will not actually change the experience points in any way.\nProgress must be within 0 and 1.")
@Examples({"""
        show xp level 12 and progress 0.5 to all players"""})
@Since("1.0.2, 1.1 (decimal xp)")
@DocumentationId("9046")
@SuppressWarnings("unused")
public class EffPlayerFakeExperience extends Effect {
    static {
        // todo: figure out if and why this element takes a bit to parse
        Skript.registerEffect(EffPlayerFakeExperience.class,
                "show [fake] [e]xp[erience] level %integer% and progress %number% to %players%",
                "show [fake] [e]xp[erience] level %integer% to %players%",
                "show [fake] [e]xp[erience] progress %number% to %players%");
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
    protected void execute(@NotNull Event event) {
        Float xp = null;
        Integer lvl = null;
        if (pattern == 0 || pattern == 2) {
            Number progressNumber = progress.getSingle(event);
            if (progressNumber == null) return;
            xp = progressNumber.floatValue();
            xp = Math.max(Math.min(xp, 1), 0);
        }
        if (pattern == 0 || pattern == 1) {
            lvl = level.getSingle(event);
            if (lvl == null) return;
            lvl = Math.max(lvl, 0);
        }
        for (Player player : players.getArray(event)) {
            if (pattern == 2 && xp != null) {
                player.sendExperienceChange(xp);
            } else if (pattern == 1 && lvl != null) {
                long calcNext = LuskUtils.getTotalNeededXP(player.getLevel() + 1);
                long calcCurrent = LuskUtils.getTotalNeededXP(player.getLevel());
                long calcFull = calcNext - calcCurrent;
                xp = (float) ((player.getTotalExperience() - calcCurrent) / calcFull);
                player.sendExperienceChange(xp, lvl);
            } else if (lvl != null && xp != null) {
                player.sendExperienceChange(xp, lvl);
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String xp, lvl, ps;
        xp = lvl = ps = "";
        if (event != null) {
            xp = progress.toString(event, debug);
            lvl = level.toString(event, debug);
            ps = players.toString(event, debug);
        }
        StringBuilder string = new StringBuilder("show fake experience");
        if (pattern == 0) string.append("level ").append(lvl).append(" and progress ").append(xp);
        else if (pattern == 1) string.append("level ").append(lvl);
        else string.append("progress ").append(xp);
        string.append(" to ").append(ps);
        return string.toString();
    }

}