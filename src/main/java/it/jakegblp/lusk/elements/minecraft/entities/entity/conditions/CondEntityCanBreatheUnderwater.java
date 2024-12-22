package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - Can Breathe Underwater")
@Description("Checks if the provided living entities can breathe underwater and will not take suffocation damage when their air supply reaches zero.")
@Examples({"if {_entity} can breathe underwater:"})
@Since("1.0.2")
@DocumentationId("11180")
public class CondEntityCanBreatheUnderwater extends PropertyCondition<LivingEntity> {

    static {
        register(CondEntityCanBreatheUnderwater.class, PropertyType.CAN, "breathe underwater", "livingentities");
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