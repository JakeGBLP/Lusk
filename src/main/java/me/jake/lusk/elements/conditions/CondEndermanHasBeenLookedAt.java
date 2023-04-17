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
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Enderman - Has Been Looked At")
@Description("Checks if an enderman has been looked at.")
@Examples({"if target has been looked at:"})
@Since("1.0.2")
public class CondEndermanHasBeenLookedAt extends Condition {
    static {
        Skript.registerCondition(CondEndermanHasBeenLookedAt.class, "%livingentity% has been (looked|stared) at",
                                                                "%livingentity% has(n't| not) been (looked|stared) at");
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
        assert event != null;
        return entityExpression.getSingle(event) + " can" + (isNegated() ? "'t" : "") + " be duplicated";
    }

    @Override
    public boolean check(@NotNull Event event) {
        LivingEntity entity = entityExpression.getSingle(event);
        if (entity instanceof Enderman enderman) {
            return isNegated() ^ enderman.hasBeenStaredAt();
        }
        return false;
    }
}