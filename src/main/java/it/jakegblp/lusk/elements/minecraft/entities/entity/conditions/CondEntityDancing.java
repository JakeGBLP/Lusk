package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Allay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Piglin;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Dancing")
@Description("Checks if an entity is dancing.\n(Parrot, Allay, Piglin)")
@Examples({"on damage of parrot:\n\tif victim is dancing:\n\t\tcancel event\n\t\tbroadcast \"The vibe won't stop!\""})
@Since("1.0.2, 1.1 (Piglin)")
public class CondEntityDancing extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityDancing.class, "dancing", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return (entity instanceof Parrot parrot && parrot.isDancing()) ||
                (entity instanceof Piglin piglin && piglin.isDancing()) ||
                (entity instanceof Allay allay && allay.isDancing());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "dancing";
    }
}