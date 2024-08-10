package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Leashed")
@Description("Checks if an entity is leashed.")
@Examples({"if target is leashed:"})
@Since("1.0.4")
@DocumentationId("11184")
public class CondEntityLeashed extends PropertyCondition<Entity> {
    static {
        register(CondEntityLeashed.class, "leashed", "entities");
    }

    @Override
    public boolean check(Entity e) {
        return e instanceof LivingEntity livingEntity && livingEntity.isLeashed();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "leashed";
    }

}