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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Goat;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goat - Has Left/Right Horn")
@Description("Checks if goat has either horn.")
@Examples({"if target has its left horn:"})
@Since("1.0.3")
public class CondGoatHasHorns extends Condition {
    static {
        Skript.registerCondition(CondGoatHasHorns.class,
                "%entity% has [its|the] left horn",
                "%entity% does(n't| not) have [its|the] left horn",
                "%entity% has [its|the] right horn",
                "%entity% does(n't| not) have [its|the] right horn");
    }

    private Expression<Entity> entityExpression;
    private boolean left;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        entityExpression = (Expression<Entity>) expressions[0];
        left = matchedPattern == 0 || matchedPattern == 1;
        setNegated(matchedPattern == 1 || matchedPattern == 3);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event == null ? "" : entityExpression.getSingle(event)) + (isNegated() ? " does not have" : " has") + " its " + (left ? "left" : "right") + "horn";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity entity = entityExpression.getSingle(event);
        if (entity instanceof Goat goat) {
            boolean bool;
            if (left) {
                bool = goat.hasLeftHorn();
            } else {
                bool = goat.hasRightHorn();
            }
            return isNegated() ^ bool;
        }
        return false;
    }
}