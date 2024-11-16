package it.jakegblp.lusk.elements.minecraft.entities.horse.conditions;

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
@SuppressWarnings("unused")
public class CondHorseEatingGrass extends PropertyCondition<LivingEntity> {
    static {
        register(CondHorseEatingGrass.class, "eating grass", "livingentity");
    }

    @Override
    public boolean check(LivingEntity livingEntity) {
        return livingEntity instanceof AbstractHorse horse && horse.isEatingGrass();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "eating grass";
    }
}