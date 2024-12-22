package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.EntityUtils.isInterested;

@Name("Animal - is Interested")
@Description("Checks if the provided foxed or wolves are interested.\nFor Foxes, Paper 1.18.2+ is required.")
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
        return isInterested(entity);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "interested";
    }
}