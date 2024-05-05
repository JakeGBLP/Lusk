package it.jakegblp.lusk.elements.minecraft.entities.passive.allay.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Allay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Allay - Can Be Duplicated")
@Description("Checks if an allay can be duplicated.")
@Examples({"if target can be duplicated:"})
@Since("1.0.2")
public class CondAllayCanBeDuplicated extends Condition {
    static {
        Skript.registerCondition(CondAllayCanBeDuplicated.class, "%livingentity% can be duplicated",
                "%livingentity% can(n't|not) be duplicated");
    }

    private Expression<LivingEntity> entityExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event != null ? entityExpression.toString(event, debug) : "") + " can" + (isNegated() ? "'t" : "") + " be duplicated";
    }

    @Override
    public boolean check(@NotNull Event event) {
        LivingEntity entity = entityExpression.getSingle(event);
        if (entity instanceof Allay allay) {
            return isNegated() ^ allay.canDuplicate();
        }
        return false;
    }
}