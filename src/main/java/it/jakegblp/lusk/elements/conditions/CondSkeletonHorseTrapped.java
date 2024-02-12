package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.SkeletonHorse;
import org.jetbrains.annotations.NotNull;

@Name("Skeleton Horse - is Trapped")
@Description("Checks if a skeleton horse is trapped.")
@Examples({"on damage of skeleton horse:\n\tif victim is trapped:\n\t\tcancel event"})
@Since("1.0.3")
public class CondSkeletonHorseTrapped extends PropertyCondition<LivingEntity> {
    static {
        register(CondSkeletonHorseTrapped.class, "trapped", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof SkeletonHorse skeletonHorse) {
            return skeletonHorse.isTrapped();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "trapped";
    }
}