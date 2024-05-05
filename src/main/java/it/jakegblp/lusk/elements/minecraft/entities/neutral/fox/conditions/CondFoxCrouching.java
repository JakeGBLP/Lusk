package it.jakegblp.lusk.elements.minecraft.entities.neutral.fox.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Fox - is Crouching")
@Description("Checks if a fox is crouching.")
@Examples({"on damage of fox:\n\tif victim is crouching:\n\t\tcancel event"})
@Since("1.0.0")
public class CondFoxCrouching extends PropertyCondition<LivingEntity> {
    static {
        register(CondFoxCrouching.class, "crouching", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Fox fox && fox.isCrouching();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "crouching";
    }
}