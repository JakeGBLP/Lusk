package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - Can Breathe Underwater")
@Description("Checks if a living entity can breathe underwater.")
@Examples({"if {_entity} can breathe underwater:"})
@Since("1.0.2")
public class CondCanBreatheUnderwater extends PropertyCondition<LivingEntity> {

    static {
        register(CondCanBreatheUnderwater.class, PropertyType.CAN, "breathe underwater", "livingentities");
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