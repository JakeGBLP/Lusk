package it.jakegblp.lusk.elements.minecraft.entities.entity.events.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity Shoot - Force")
@Description("The force the arrow was launched with in the Entity Shoot Event.\nThis number ranges from 0 to 0.")
@Examples("on entity shoot:\n\tbroadcast the bow force")
@Since("1.1.1")
public class ExprEntityShootForce extends SimpleExpression<Number> {
    static {
        Skript.registerExpression(ExprEntityShootForce.class, Number.class, ExpressionType.SIMPLE,
                "[the |event-](bow [shoot]|arrow [launch|shot]|sho[o]t) force");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(EntityShootBowEvent.class)) {
            Skript.error("This expression can only be used in the Entity Shoot event!");
            return false;
        }
        return true;
    }

    @Override
    protected Number @NotNull [] get(@NotNull Event e) {
        return new Number[]{((EntityShootBowEvent) e).getForce()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the shot force";
    }
}
