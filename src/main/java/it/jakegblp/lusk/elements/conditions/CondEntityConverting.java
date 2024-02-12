package it.jakegblp.lusk.elements.conditions;

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
        if (entity instanceof Hoglin hoglin) {
            return hoglin.isConverting();
        } else if (entity instanceof Husk husk) {
            return husk.isConverting();
        } else if (entity instanceof Piglin piglin) {
            return piglin.isConverting();
        } else if (entity instanceof PigZombie pigZombie) {
            return pigZombie.isConverting();
        } else if (entity instanceof Skeleton skeleton) {
            return skeleton.isConverting();
        } else if (entity instanceof ZombieVillager zombieVillager) {
            return zombieVillager.isConverting();
        } else if (entity instanceof Zombie zombie) {
            return zombie.isConverting();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "converting";
    }
}