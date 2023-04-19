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
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Cat - Has Its Head Up")
@Description("Checks if a cat has its head up.")
@Examples({"on damage:\n\tif victim has its head up:\n\t\tbroadcast \"it's looking up!\""})
@Since("1.0.3")
public class CondCatHasHeadUp extends Condition {
    static {
        Skript.registerCondition(CondCatHasHeadUp.class, "%entity% has [a] its head up",
                                                             "%entity% does(n't| not) have [a] its head up");
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
        return (event == null ? "" : entityExpression.getSingle(event)) + (isNegated() ? " does not have" : " has") + " its head up";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity entity = entityExpression.getSingle(event);
        if (entity instanceof Cat cat) {
            return isNegated() ^ cat.isHeadUp();
        }
        return false;
    }
}