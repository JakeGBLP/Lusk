package me.jake.lusk.elements.conditions;

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
        if (livingEntity instanceof AbstractHorse horse) {
            return horse.isRearing();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "rearing";
    }
}