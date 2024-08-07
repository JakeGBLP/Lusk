package it.jakegblp.lusk.elements.minecraft.entities.other.projectiles.trident.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.jetbrains.annotations.NotNull;

@Name("Trident - has Collided")
@Description("Checks if one or more (thrown) tridents have collided (they must have dealt damage to an entity or have hit the floor).")
@Examples({"if {_trident} has collided:"})
@Since("1.2")
public class CondTridentCollided extends PropertyCondition<Projectile> {

    static {
        register(CondTridentCollided.class, PropertyType.HAVE, "collided", "projectiles");
    }

    @Override
    public boolean check(Projectile projectile) {
        return projectile instanceof Trident trident && trident.hasDealtDamage();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "collided";
    }

}