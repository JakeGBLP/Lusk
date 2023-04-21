package me.jake.lusk.elements.expressions;

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

@Name("Entity - Raised Hand Time")
@Description("Returns how long an entity's hand has been raised for.")
@Examples({"broadcast raised hand time of target"})
@Since("1.0.3")
public class ExprEntityRaisedHandTime extends SimplePropertyExpression<Entity, Timespan> {
    static {
        register(ExprEntityRaisedHandTime.class, Timespan.class, "raised hand time", "entity");
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    @Nullable
    public Timespan convert(Entity e) {
        if (e instanceof LivingEntity livingEntity) {
            return Timespan.fromTicks_i(livingEntity.getHandRaisedTime());
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "raised hand time";
    }
}