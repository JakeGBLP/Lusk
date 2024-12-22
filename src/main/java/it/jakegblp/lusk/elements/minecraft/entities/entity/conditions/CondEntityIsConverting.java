package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.EntityUtils.isConverting;

@Name("Entity - is Converting")
@Description("Checks if the provided entities being converted.\n(Hoglin, Husk, Piglin, Pig Zombie, Skeleton, Zombie, Zombie Villager)")
@Examples({"on damage of piglin:\n\tif victim is converting:\n\t\tcancel event"})
@Since("1.0.3")
@DocumentationId("9113")
public class CondEntityIsConverting extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityIsConverting.class, "converting", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return isConverting(entity);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "converting";
    }
}