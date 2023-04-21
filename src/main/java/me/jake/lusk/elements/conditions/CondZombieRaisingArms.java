package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.NotNull;

@Name("Zombie - is Raising Its Arms")
@Description("Checks if a zombie is raising its arms.")
@Examples({"if target is raising its arms:"})
@Since("1.0.3")
public class CondZombieRaisingArms extends PropertyCondition<LivingEntity> {
    static {
        register(CondZombieRaisingArms.class, "raising its arms", "livingentity");
    }

    @Override
    public boolean check(LivingEntity livingEntity) {
        if (livingEntity instanceof Zombie zombie) {
            return zombie.isArmsRaised();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "raising its arms";
    }
}