package it.jakegblp.lusk.elements.minecraft.entities.projectile.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Projectile;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_19_2_EXTENDED_ENTITY_API;

@Name("Projectile - has Left The Shooter (Property)")
@Description("Gets whether the provided projectiles have left the hitbox of their shooter and can now hit entities.\nThis is recalculated each tick if the projectile has a shooter.\nCan be set.")
@Examples("set whether {_projectile} has already left its shooter to true")
@Since("1.3")
@RequiredPlugins("Paper 1.19.2+")
public class ExprProjectileHasLeftShooter extends SimpleBooleanPropertyExpression<Projectile> {

    static {
        if (PAPER_HAS_1_19_2_EXTENDED_ENTITY_API)
            register(ExprProjectileHasLeftShooter.class, Boolean.class, "[projectile]", "[has] [already] left [the[ir]|its] shooter", "projectiles");
    }

    @Override
    public Boolean convert(Projectile from) {
        return from.hasLeftShooter();
    }

    @Override
    protected String getPropertyName() {
        return "left shooter";
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(Projectile from, Boolean to) {
        from.setHasLeftShooter(to);
    }
}
