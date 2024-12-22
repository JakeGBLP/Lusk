package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.EntityUtils.isDancing;

@Name("Entity - is Dancing")
@Description("Checks if an entity is dancing.\n(Parrot, Allay, Piglin)")
@Examples({"on damage of parrot:\n\tif victim is dancing:\n\t\tcancel event\n\t\tbroadcast \"The vibe won't stop!\""})
@Since("1.0.2, 1.1 (Piglin)")
@DocumentationId("11183")
public class CondEntityIsDancing extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityIsDancing.class, "dancing", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return isDancing(entity);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "dancing";
    }
}