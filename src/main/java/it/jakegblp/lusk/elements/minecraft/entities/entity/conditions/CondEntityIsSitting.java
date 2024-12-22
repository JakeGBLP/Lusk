package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.EntityUtils.isSitting;

@Name("Animal - is Sitting")
@Description("Checks if an entity is sitting.\n(Camels, Cats, Wolves, Parrots, Pandas and Foxes)")
@Examples({"on damage of wolf, cat or fox:\n\tif victim is sitting:\n\t\tcancel event"})
@Since("1.0.0")
@DocumentationId("8789")
@SuppressWarnings("unused")
public class CondEntityIsSitting extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityIsSitting.class, "s(at [down]|it[ting [down]])", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return isSitting(entity);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "sitting";
    }
}
