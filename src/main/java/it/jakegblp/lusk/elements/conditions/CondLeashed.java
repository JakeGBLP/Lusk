package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Leashed")
@Description("Checks if an entity is leashed.")
@Examples({"if target is leashed:"})
@Since("1.0.4")
public class CondLeashed extends PropertyCondition<Entity> {
    static {
        register(CondLeashed.class, "leashed", "entities");
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