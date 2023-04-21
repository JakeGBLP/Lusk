package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Zombie - Should Burn In Day")
@Description("Checks if a zombie should burn in daylight.")
@Examples({"if target should burn in daylight:"})
@Since("1.0.3")
public class CondZombieShouldBurnInDay extends Condition {
    static {
        Skript.registerCondition(CondZombieShouldBurnInDay.class,
                "%entity% should burn (in|during) [the] day[light]",
                "%entity% should(n't| not) burn (in|during) [the] day[light]");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parser) {
        entityExpression = (Expression<Entity>) expressions[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event == null ? "" : entityExpression.getSingle(event)) + " should " + (isNegated() ? "not " : "") + "burn in daylight";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity entity = entityExpression.getSingle(event);
        if (entity instanceof Zombie zombie) {
            return zombie.shouldBurnInDay();
        }
        return false;
    }
}