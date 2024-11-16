package it.jakegblp.lusk.elements.minecraft.entities.goat.conditions;

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
@SuppressWarnings("unused")
public class CondGoatHasHorns extends Condition {
    static {
        Skript.registerCondition(CondGoatHasHorns.class,
                "%entity% has [its|the] (:left|right) horn",
                "%entity% does(n't| not) have [its|the] (:left|right) horn");
    }

    private Expression<Entity> entityExpression;
    private boolean left;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        entityExpression = (Expression<Entity>) expressions[0];
        left = parser.hasTag("left");
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event == null ? "" : entityExpression.toString(event, debug)) + (isNegated() ? " does not have" : " has") + " its " + (left ? "left" : "right") + "horn";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity entity = entityExpression.getSingle(event);
        if (entity instanceof Goat goat) {
            return isNegated() ^ left ? goat.hasLeftHorn() : goat.hasRightHorn();
        }
        return false;
    }
}