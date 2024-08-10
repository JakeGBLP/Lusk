package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - Can Breathe Underwater")
@Description("Checks if a living entity can breathe underwater.")
@Examples({"if {_entity} can breathe underwater:"})
@Since("1.0.2")
@DocumentationId("11180")
public class CondEntityBreatheUnderwater extends PropertyCondition<LivingEntity> {

    static {
        register(CondEntityBreatheUnderwater.class, PropertyType.CAN, "breathe underwater", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity.canBreatheUnderwater();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "breathe underwater";
    }

}