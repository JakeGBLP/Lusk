package it.jakegblp.lusk.elements.minecraft.entities.dolphin.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;

@Name("Dolphin - Has Been Fed Fish")
@Description("Checks if a dolphin has a fish.")
@Examples({"on damage:\n\tif victim has a fish:\n\t\tbroadcast \"It has a fish!\""})
@Since("1.0.3")
@RequiredPlugins("Paper 1.18.2+")
@SuppressWarnings("unused")
public class CondDolphinBeenFedFish extends PropertyCondition<LivingEntity> {

    static {
        if (PAPER_1_18_2)
            register(CondDolphinBeenFedFish.class, PropertyType.HAVE, "been fed [a] fish", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Dolphin dolphin && dolphin.hasFish();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "been fed fish";
    }

}