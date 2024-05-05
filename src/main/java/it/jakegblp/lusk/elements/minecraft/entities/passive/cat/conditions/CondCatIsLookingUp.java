package it.jakegblp.lusk.elements.minecraft.entities.passive.cat.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Cat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Cat - Has Its Head Up")
@Description("Checks if a cat has its head up.")
@Examples({"on damage:\n\tif victim has its head up:\n\t\tbroadcast \"it's looking up!\""})
@Since("1.0.3")
public class CondCatIsLookingUp extends PropertyCondition<LivingEntity> {

    static {
        register(CondCatIsLookingUp.class, PropertyType.BE, "looking up", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Cat cat && cat.isHeadUp();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "looking up";
    }

}