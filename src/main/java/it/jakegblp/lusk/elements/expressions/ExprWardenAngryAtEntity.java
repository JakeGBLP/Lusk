package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Warden;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Warden - Prey")
@Description("""
        Returns the entity at which this warden is most angry.""")
@Examples({"broadcast prey of target"})
@Since("1.0.2")
public class ExprWardenAngryAtEntity extends SimplePropertyExpression<Entity, LivingEntity> {
    static {
        register(ExprWardenAngryAtEntity.class, LivingEntity.class, "prey", "entity");
    }

    @Override
    public @NotNull Class<? extends LivingEntity> getReturnType() {
        return LivingEntity.class;
    }

    @Override
    @Nullable
    public LivingEntity convert(Entity e) {
        if (e instanceof Warden warden) {
            return warden.getEntityAngryAt();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "prey";
    }
}