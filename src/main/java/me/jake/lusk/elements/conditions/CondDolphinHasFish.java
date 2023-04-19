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
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Dolphin - Has Fish")
@Description("Checks if a dolphin has a fish.")
@Examples({"on damage:\n\tif victim has a fish:\n\t\tbroadcast \"It has a fish!\""})
@Since("1.0.3")
public class CondDolphinHasFish extends Condition {
    static {
        Skript.registerCondition(CondDolphinHasFish.class, "%entity% has [a] fish",
                                                             "%entity% does(n't| not) have [a] fish");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        entityExpression = (Expression<Entity>) expressions[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event == null ? "" : entityExpression.getSingle(event)) + (isNegated() ? " does not have" : " has") + " a fish";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity entity = entityExpression.getSingle(event);
        if (entity instanceof Dolphin dolphin) {
            return isNegated() ^ dolphin.hasFish();
        }
        return false;
    }
}