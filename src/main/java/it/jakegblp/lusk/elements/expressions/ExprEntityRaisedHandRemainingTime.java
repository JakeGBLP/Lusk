package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Raised Hand Remaining Time")
@Description("Returns the remaining time an entity needs to keep hands raised with an item to finish using it.")
@Examples({"broadcast remaining raised hand time of target"})
@Since("1.0.3")
public class ExprEntityRaisedHandRemainingTime extends SimplePropertyExpression<Entity, Timespan> {
    static {
        register(ExprEntityRaisedHandRemainingTime.class, Timespan.class, "remaining raised hand time", "entity");
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    @Nullable
    public Timespan convert(Entity e) {
        if (e instanceof LivingEntity livingEntity) {
            return Timespan.fromTicks_i(livingEntity.getItemUseRemainingTime());
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "remaining raised hand time";
    }
}