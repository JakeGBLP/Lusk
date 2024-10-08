package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Underwater")
@Description("Checks if an entity is underwater.")
@Examples({"if player is underwater:"})
@Since("1.0.2")
@DocumentationId("9026")
public class CondEntityUnderwater extends PropertyCondition<Entity> {

    static {
        register(CondEntityUnderwater.class, "underwater", "entities");
    }

    @Override
    public boolean check(Entity entity) {
        return entity.isUnderWater();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "underwater";
    }
}