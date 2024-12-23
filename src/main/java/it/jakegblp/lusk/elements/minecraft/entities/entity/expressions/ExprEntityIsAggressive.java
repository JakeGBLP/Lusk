package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_2;

@Name("Entity - is Aggressive (Property)")
@Description("""
        Returns the Aggressive Property of an entity.
        Can be set.
        
        Some mobs will raise their arm(s) when aggressive:
        Drowned, Piglin, Skeleton, Zombie, ZombieVillager, Illusioner, Vindicator, Panda, Pillager, PiglinBrute

        Note: This doesn't always return the actual aggressive state as when set, Pandas are always aggressive if their combined Panda Gene is AGGRESSIVE.""")
@Examples({"broadcast is aggressive state of target"})
@Since("1.3")
@RequiredPlugins("Paper 1.20.2+")
@SuppressWarnings("unused")
public class ExprEntityIsAggressive extends SimpleBooleanPropertyExpression<LivingEntity> {

    static { // simulate this?
        if (MINECRAFT_1_20_2)
            register(ExprEntityIsAggressive.class, Boolean.class, "[is] aggressive", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Mob mob && mob.isAggressive();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Mob mob)
            mob.setAggressive(to);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "is aggressive";
    }
}