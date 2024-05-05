package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Converting")
@Description("Checks if an entity is being converted.\n(Hoglin, Husk, Piglin, Pig Zombie, Skeleton, Zombie, Zombie Villager)")
@Examples({"on damage of enderman:\n\tif victim is screaming:\n\t\tcancel event"})
@Since("1.0.3")
public class CondEntityConverting extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityConverting.class, "converting", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return (entity instanceof ZombieVillager zombieVillager && zombieVillager.isConverting()) ||
                (entity instanceof PiglinAbstract piglin && piglin.isConverting()) ||
                (entity instanceof Skeleton skeleton && skeleton.isConverting()) ||
                (entity instanceof Hoglin hoglin && hoglin.isConverting()) ||
                (entity instanceof Zombie zombie && zombie.isConverting()) ||
                (entity instanceof Husk husk && husk.isConverting());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "converting";
    }
}