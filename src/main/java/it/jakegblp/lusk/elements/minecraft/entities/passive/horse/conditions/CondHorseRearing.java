package it.jakegblp.lusk.elements.minecraft.entities.passive.horse.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Horse - is Rearing")
@Description("Checks if an horse is rearing.")
@Examples({"if target is rearing:"})
@Since("1.0.3")
public class CondHorseRearing extends PropertyCondition<LivingEntity> {
    static {
        register(CondHorseRearing.class, "rearing", "livingentity");
    }

    @Override
    public boolean check(LivingEntity livingEntity) {
        return livingEntity instanceof AbstractHorse horse && horse.isRearing();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "rearing";
    }
}