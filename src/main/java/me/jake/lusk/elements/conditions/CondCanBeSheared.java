package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.papermc.paper.entity.Shearable;
import me.jake.lusk.utils.Utils;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Name("Entity - Can Be Sheared")
@Description("Checks if an entity is ready to be sheared.\n\nWhen using Paper this condition can be used with mushroom cows and snow golems.")
@Examples({"if target can be sheared:"})
@Since("1.0.2")
public class CondCanBeSheared extends Condition {
    static {
        Skript.registerCondition(CondCanBeSheared.class, "%entity% can be (sheared|shorn)",
                                                                "%entity% can(n't|not) be (sheared|shorn)",
                                                                "%entity% is (sheared|shorn)",
                                                                "%entity% is(n't| not) (sheared|shorn)");
    }

    private Expression<Entity> entityExpression;
    private boolean paper;
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (Skript.classExists("io.papermc.paper.entity.Shearable")) {
            paper = true;
        }
        entityExpression = (Expression<Entity>) expressions[0];
        setNegated(matchedPattern == 1 || matchedPattern == 2);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        assert event != null;
        return entityExpression.getSingle(event) + " can" + (isNegated() ? "'t" : "") + " be sheared";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity entity = entityExpression.getSingle(event);
        if (paper) {
            if (Utils.isShearable(EntityUtils.toSkriptEntityData(Objects.requireNonNull(entity).getType()))) {
                return isNegated() ^ ((Shearable) Objects.requireNonNull(entity)).readyToBeSheared();
            }
        } else if (entity instanceof Sheep sheep) {
            return isNegated() ^ !sheep.isSheared();
        } else if (entity instanceof Snowman snowman) {
            return isNegated() ^ !snowman.isDerp();
        } else if (entity instanceof MushroomCow) {
            return isNegated();
        } else if (entity instanceof Cow) {
            return !isNegated();
    }
        return false;
    }
}