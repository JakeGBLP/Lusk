package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_2;

@Name("Entity - is Aggressive")
@Description("""
        Checks if the provided entities are aggressive.
        
        Some mobs will raise their arm(s) when aggressive:
        Drowned, Piglin, Skeleton, Zombie, ZombieVillager, Illusioner, Vindicator, Panda, Pillager, PiglinBrute

        Note: This doesn't always return the actual aggressive state as when set, Pandas are always aggressive if their combined Panda Gene is AGGRESSIVE.
        """)
@Examples({"on damage of wolf:\n\tif victim is angry:\n\t\tcancel event"})
@Since("1.3")
@RequiredPlugins("Paper 1.20.2")
@SuppressWarnings("unused")
// todo: hostile api: https://github.com/PaperMC/Paper/commit/9ee60eca7d75ad2eeec88ec80d7f743b40bedf0f
public class CondEntityIsAggressive extends PropertyCondition<LivingEntity> {
    static { // todo: UTILS + https://github.com/PaperMC/Paper/commit/9ee60eca7d75ad2eeec88ec80d7f743b40bedf0f#diff-919807150de2d8e3011e711f4e29fe3339f00e4a8f7d9c25984798edc875c887
        if (MINECRAFT_1_20_2)
            register(CondEntityIsAggressive.class, "aggressive", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Mob mob && mob.isAggressive();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "aggressive";
    }
}