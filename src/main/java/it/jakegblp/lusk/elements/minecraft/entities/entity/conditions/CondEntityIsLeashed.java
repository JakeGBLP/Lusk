package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Leashed")
@Description("Checks if an entity is leashed.")
@Examples({"if target is leashed:"})
@Since("1.0.4")
@DocumentationId("11184")
public class CondEntityIsLeashed extends PropertyCondition<LivingEntity> {

    static {
        register(CondEntityIsLeashed.class, "leashed", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity.isLeashed();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "leashed";
    }

}