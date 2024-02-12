package it.jakegblp.lusk.elements.conditions;

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
import org.bukkit.entity.Llama;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Llama - Has Caravan Tail")
@Description("Checks if another llama is currently following behind this llama.")
@Examples({"on damage:\n\tif victim has a caravan tail:\n\t\tbroadcast \"the caravan is being disturbed!!\""})
@Since("1.0.3")
public class CondLlamaHasCaravanTail extends Condition {
    static {
        Skript.registerCondition(CondLlamaHasCaravanTail.class, "%entity% has [a] caravan tail",
                "%entity% does(n't| not) have [a] caravan tail");
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
        return (event == null ? "" : entityExpression.getSingle(event)) + (isNegated() ? " does not have" : " has") + " a caravan tail";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity entity = entityExpression.getSingle(event);
        if (entity instanceof Llama llama) {
            return isNegated() ^ llama.hasCaravanTail();
        }
        return false;
    }
}