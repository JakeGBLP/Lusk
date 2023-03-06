package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("is Crouching")
@Description("Checks if a fox is crouching.")
@Examples({"on damage of fox:\n\tif victim is crouching:\n\t\tcancel event"})
@Since("1.0.0")
public class CondCrouching extends PropertyCondition<LivingEntity> {

    static {
        register(CondCrouching.class, "crouching", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity != null) {
            if (entity.getType() == EntityType.FOX) {
                return ((Fox) entity).isCrouching();
            } else {
                Skript.error("You can only use this condition with Foxes!");
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "crouching";
    }
}