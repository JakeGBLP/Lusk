package it.jakegblp.lusk.elements.minecraft.entities.vindicator.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vindicator;
import org.jetbrains.annotations.NotNull;

@Name("Vindicator - is Johnny")
@Description("Checks if a vindicator is Johnny.")
@Examples({"if {_vindicator} is johnny:"})
@Since("1.2")
@SuppressWarnings("unused")
public class CondVindicatorJohnny extends PropertyCondition<LivingEntity> {
    static {
        register(CondVindicatorJohnny.class, "johnny", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Vindicator vindicator && vindicator.isJohnny();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "johnny";
    }
}