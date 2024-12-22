package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is In Powdered Snow")
@Description("Checks if an entity is in powdered snow.")
@Examples({"if player is in powdered snow:"})
@Since("1.0.2, 1.3 (Plural)")
@DocumentationId("9024")
public class CondEntityIsInPowderedSnow extends PropertyCondition<Entity> {

    static {
        register(CondEntityIsInPowderedSnow.class, "in powdered snow", "entities");
    }

    @Override
    public boolean check(Entity entity) {
        return entity.isInPowderedSnow();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "in powdered snow";
    }

}