package it.jakegblp.lusk.elements.minecraft.entities.trident.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.jetbrains.annotations.NotNull;

@Name("Trident - is Enchanted")
@Description("Checks if one or more (thrown) tridents are enchanted.")
@Examples({"if {_trident} is enchanted:"})
@Since("1.2")
@SuppressWarnings("unused")
public class CondTridentEnchanted extends PropertyCondition<Projectile> {

    static {
        register(CondTridentEnchanted.class, "enchanted", "projectiles");
    }

    @Override
    public boolean check(Projectile projectile) {
        return projectile instanceof Trident trident && trident.hasGlint();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "enchanted";
    }

}