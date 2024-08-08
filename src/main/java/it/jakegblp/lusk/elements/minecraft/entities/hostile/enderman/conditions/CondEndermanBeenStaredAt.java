package it.jakegblp.lusk.elements.minecraft.entities.hostile.enderman.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.elements.minecraft.entities.neutral.dolphin.conditions.CondDolphinBeenFedFish;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Enderman - Has Been Stared At")
@Description("Checks if an enderman has been stared at.")
@Examples({"if target has been stared at:"})
@Since("1.0.2")
public class CondEndermanBeenStaredAt extends PropertyCondition<LivingEntity> {

    static {
        register(CondEndermanBeenStaredAt.class, PropertyType.HAVE, "been stared at", "livingentities");
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