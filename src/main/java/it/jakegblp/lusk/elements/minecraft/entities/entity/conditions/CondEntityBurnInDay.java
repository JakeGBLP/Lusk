package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Should Burn In Day")
@Description("Checks if an Entity should burn in daylight.\n(Zombie,Phantom,Skeleton)\n\nFor skeletons, this does not take into account the entity's natural fire immunity.")
@Examples({"if target should burn in daylight:"})
@Since("1.0.3, 1.1.1 (Skeleton,Phantom)")
public class CondEntityBurnInDay extends Condition {
    static {
        Skript.registerCondition(CondEntityBurnInDay.class, "%livingentity% should[not:(n't| not)] burn (in|during) [the] day[light]");
    }

    private Expression<LivingEntity> entityExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        setNegated(parser.hasTag("not"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event != null ? entityExpression.toString(event, debug) : "") + "should" + (isNegated() ? " not" : "") + " burn during the day";
    }

    @Override
    public boolean check(@NotNull Event event) {
        LivingEntity entity = entityExpression.getSingle(event);
        boolean should = (entity instanceof Zombie zombie && zombie.shouldBurnInDay()) ||
                (entity instanceof AbstractSkeleton skeleton && skeleton.shouldBurnInDay()) ||
                (entity instanceof Phantom phantom && phantom.shouldBurnInDay());
        return isNegated() ^ should;
    }
}