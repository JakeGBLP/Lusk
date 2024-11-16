package it.jakegblp.lusk.elements.minecraft.entities.trident.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.jetbrains.annotations.NotNull;

@Name("Trident - is Loyal")
@Description("Checks if one or more (thrown) tridents are loyal.")
@Examples({"if {_trident} is loyal:"})
@Since("1.2")
@SuppressWarnings("unused")
public class CondTridentLoyal extends PropertyCondition<Projectile> {

    static {
        register(CondTridentLoyal.class, "loyal", "projectiles");
    }

    @Override
    public boolean check(Projectile projectile) {
        return projectile instanceof Trident trident && trident.getLoyaltyLevel() > 0;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "loyal";
    }

}