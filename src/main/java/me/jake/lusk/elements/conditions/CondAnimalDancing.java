package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Allay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.jetbrains.annotations.NotNull;

@Name("Animal - is Dancing")
@Description("Checks if an entity is dancing.\n(Parrot, Allay)")
@Examples({"on damage of parrot:\n\tif victim is dancing:\n\t\tcancel event\n\t\tbroadcast \"The vibe won't stop!\""})
@Since("1.0.2")
public class CondAnimalDancing extends PropertyCondition<LivingEntity> {
    static {
        register(CondAnimalDancing.class, "dancing", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Parrot parrot) {
            return parrot.isDancing();
        } else if (entity instanceof Allay allay) {
            return allay.isDancing();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "dancing";
    }
}