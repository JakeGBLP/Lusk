package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.NotNull;

@Name("Animal - is Interested")
@Description("Checks if an entity is interested.\n(Wolf, Fox)")
@Examples({"on damage of wolf:\n\tif victim is interested:\n\t\tcancel event"})
@Since("1.0.0")
@DocumentationId("8796")
@SuppressWarnings("unused")
public class CondEntityInterested extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityInterested.class, "interested", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Fox fox) {
            return fox.isInterested();
        } else if (entity instanceof Wolf wolf) {
            return wolf.isInterested();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "interested";
    }
}