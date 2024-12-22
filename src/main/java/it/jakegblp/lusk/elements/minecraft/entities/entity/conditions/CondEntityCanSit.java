package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sittable;
import org.jetbrains.annotations.NotNull;

@Name("Entity - Can Sit")
@Description("Checks if an entity can normally sit.")
@Examples("if target can sit:")
@Since("1.0.2, 1.2 (Plural)")
@DocumentationId("9039")
public class CondEntityCanSit extends PropertyCondition<Entity> {

    static {
        register(CondEntityCanSit.class, PropertyType.CAN, "sit", "entities");
    }

    @Override
    public boolean check(Entity entity) {
        return entity instanceof Sittable;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "sit";
    }
}
