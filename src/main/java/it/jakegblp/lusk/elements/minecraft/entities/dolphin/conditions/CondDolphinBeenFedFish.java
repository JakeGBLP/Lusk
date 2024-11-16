package it.jakegblp.lusk.elements.minecraft.entities.dolphin.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Dolphin - Has Fish")
@Description("Checks if a dolphin has a fish.")
@Examples({"on damage:\n\tif victim has a fish:\n\t\tbroadcast \"It has a fish!\""})
@Since("1.0.3")
@SuppressWarnings("unused")
public class CondDolphinBeenFedFish extends PropertyCondition<LivingEntity> {

    static {
        register(CondDolphinBeenFedFish.class, PropertyType.HAVE, "been fed fish", "livingentities");
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