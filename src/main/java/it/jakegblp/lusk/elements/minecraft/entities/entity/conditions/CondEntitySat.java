package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sittable;
import org.jetbrains.annotations.NotNull;

@Name("Animal - is Sitting")
@Description("Checks if an entity is sitting.\n(Cats, Wolves, Parrots, Pandas and Foxes)")
@Examples({"on damage of wolf, cat or fox:\n\tif victim is sitting:\n\t\tcancel event"})
@Since("1.0.0")
@DocumentationId("8789")
@SuppressWarnings("unused")
public class CondEntitySat extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntitySat.class, "s(at [down]|it[ting [down]])", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Sittable sittable) return sittable.isSitting();
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "sitting";
    }
}
