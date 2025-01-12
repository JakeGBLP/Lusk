package it.jakegblp.lusk.elements.minecraft.entities.irongolem.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Iron Golem - Created By a Player")
@Description("Checks if an iron golem was created by a player.")
@Examples({"if {_golem} was created by a player:"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class CondIronGolemCreatedByPlayer extends Condition {
    static {
        Skript.registerCondition(CondIronGolemCreatedByPlayer.class,
                "%livingentities% was[not:(n't| not)] (created|built) by ([a] player|players)");
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
        return entityExpression.toString(event, debug) + "were " + (isNegated() ? "not " : "") + "created by a player";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return entityExpression.check(event, o -> o instanceof IronGolem ironGolem && ironGolem.isPlayerCreated());
    }
}