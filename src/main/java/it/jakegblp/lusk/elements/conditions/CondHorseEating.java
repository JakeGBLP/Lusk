package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Horse - is Eating")
@Description("Checks if an horse is eating.")
@Examples({"if target is eating:"})
@Since("1.0.3")
public class CondHorseEating extends PropertyCondition<LivingEntity> {
    static {
        register(CondHorseEating.class, "eating", "livingentity");
    }

    @Override
    public boolean check(LivingEntity livingEntity) {
        if (livingEntity instanceof AbstractHorse horse) {
            return horse.isEating();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "eating";
    }
}