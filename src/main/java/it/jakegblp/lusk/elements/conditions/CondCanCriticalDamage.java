package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Player - Can Critical Damage")
@Description("Checks if a player is in position to inflict a critical hit.\n\nRead https://minecraft.fandom.com/wiki/Damage#Critical_hit for more info.")
@Examples({"if player can critical damage:"})
@Since("1.0.2")
public class CondCanCriticalDamage extends Condition {
    static {
        Skript.registerCondition(CondCanCriticalDamage.class,
                "%player% can [[inflict] a] critical [damage] (hit|attack)",
                "%player% can [inflict] critical damage",
                "%player% can critically (hit|attack|damage)",
                "%player% can(not|n't) [[inflict] a] critical [damage] (hit|attack)",
                "%player% can(not|n't) [inflict] critical damage",
                "%player% can(not|n't) critically (hit|attack|damage)");
    }

    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parser) {
        playerExpression = (Expression<Player>) expressions[0];
        setNegated((matchedPattern == 3) || (matchedPattern == 4) || (matchedPattern == 5));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        Player player = null;
        if (event != null) {
            player = playerExpression.getSingle(event);
        }
        return (player != null ? player : "") + " can" + (isNegated() ? "'t" : "") + " inflict a critical hit";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity entity = playerExpression.getSingle(event);
        return isNegated() ^ Utils.canCriticalDamage(entity);
    }
}