package it.jakegblp.lusk.elements.minecraft.entities.enderman.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;

@Name("Enderman - Has Been Stared At")
@Description("Checks if an enderman has been stared at.")
@Examples({"if target has been stared at:"})
@Since("1.0.2")
@DocumentationId("9021")
@RequiredPlugins("Paper 1.18.2+")
@SuppressWarnings("unused")
public class CondEndermanHasBeenStaredAt extends PropertyCondition<LivingEntity> {

    static {
        if (PAPER_1_18_2)
            register(CondEndermanHasBeenStaredAt.class, PropertyType.HAVE, "been stared at", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Enderman enderman && enderman.hasBeenStaredAt();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "been stared at";
    }
}