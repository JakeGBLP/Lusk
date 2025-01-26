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
import it.jakegblp.lusk.utils.LuskUtils;
import org.bukkit.entity.Goat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.CompatibilityUtils.test;

@Name("Goat - Has Left/Right/Both/Either Horn")
@Description("Checks if goat has the left, right, both or either horn.")
@Examples({"if target has its left horn:"})
@Since("1.0.3, 1.3 (Plural, Both/Either)")
@SuppressWarnings("unused")
public class CondGoatHasHorns extends Condition {
    static { // todo: property expression
        Skript.registerCondition(CondGoatHasHorns.class,
                "%livingentities% (has|have) [its|the[ir]] (:left|:right|either|any) [goat] horn",
                "%livingentities% (doesn't|does not|do not|don't) have [its|the[ir]] (:left|:right|either|any) [goat] horn",
                "%livingentities% (has|have) [its|the[ir]|both] [goat] horns",
                "%livingentities% (doesn't|does not|do not|don't) have [its|the[ir]|both] [goat] horns"
        );
    }

    private Expression<LivingEntity> entityExpression;
    /**
     * TRUE = left; FALSE = right; UNKNOWN = either
     */
    private Kleenean left = null;
    private boolean both = false;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        if (matchedPattern > 1) both = true;
        else left = LuskUtils.getKleenean(parser.hasTag("left"), parser.hasTag("right"));
        setNegated((matchedPattern % 2) != 0);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return entityExpression.toString(event, debug) + (isNegated() ? " does not have " : " has ")
                + (both ? "both horns" : switch (left) {
                    case TRUE -> "its left horn";
                    case FALSE -> "its right horn";
                    case UNKNOWN -> "either horn";
        });
    }

    @Override
    public boolean check(@NotNull Event event) {
        return test(entityExpression, event, entity -> entity instanceof Goat goat
                && (both ? goat.hasLeftHorn() && goat.hasRightHorn() : switch (left) {
                    case TRUE -> goat.hasLeftHorn();
                    case FALSE -> goat.hasRightHorn();
                    case UNKNOWN -> goat.hasLeftHorn() || goat.hasRightHorn();
                }));
    }
}