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
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Can Breathe Underwater")
@Description("Checks if a living entity can breathe underwater.")
@Examples({"if {_entity} can breathe underwater:"})
@Since("1.0.2")
public class CondCanBreathUnderwater extends Condition {
    static {
        Skript.registerCondition(CondCanBreathUnderwater.class, "%livingentity% can breathe underwater", "%livingentity% can(no| n')t breathe underwater");
    }

    private Expression<LivingEntity> livingEntityExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parser) {
        setNegated(matchedPattern == 1);
        livingEntityExpression = (Expression<LivingEntity>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event == null ? "" : livingEntityExpression.getSingle(event)) + " can" + (isNegated() ? "not" : "") + " breathe underwater";
    }

    @Override
    public boolean check(@NotNull Event event) {
        LivingEntity entity = livingEntityExpression.getSingle(event);
        if (entity != null) {
            return isNegated() ^ entity.canBreatheUnderwater();
        }
        return false;
    }
}