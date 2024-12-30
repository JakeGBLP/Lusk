package it.jakegblp.lusk.elements.minecraft.entities.projectile.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Projectile;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_19_2_EXTENDED_ENTITY_API;

@Name("Projectile - has Left The Shooter")
@Description("Gets whether the provided projectiles have left the hitbox of their shooter and can now hit entities.\nThis is recalculated each tick if the projectile has a shooter.")
@Examples("if {_projectile} has left its shooter:")
@Since("1.3")
@RequiredPlugins("Paper 1.19.2+")
public class CondProjectileHasLeftShooter extends PropertyCondition<Projectile> {

    static {
        if (PAPER_HAS_1_19_2_EXTENDED_ENTITY_API)
            register(CondProjectileHasLeftShooter.class, PropertyType.HAVE, "[already] left [the[ir]|its] shooter", "projectiles");
    }

    @Override
    public boolean check(Projectile value) {
        return value.hasLeftShooter();
    }

    @Override
    protected String getPropertyName() {
        return "left the shooter";
    }
}
