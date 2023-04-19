package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Horse - is Eating Grass")
@Description("Checks if an horse is eating grass.")
@Examples({"if target is eating grass:"})
@Since("1.0.3")
public class CondHorseEatingGrass extends PropertyCondition<LivingEntity> {
    static {
        register(CondHorseEatingGrass.class, "eating grass", "livingentity");
    }

    @Override
    public boolean check(LivingEntity livingEntity) {
        if (livingEntity instanceof AbstractHorse horse) {
            return horse.isEatingGrass();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "eating grass";
    }
}