package it.jakegblp.lusk.elements.minecraft.entities.cat.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Cat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;

@Name("Cat - is Looking Up")
@Description("Checks if a cat is looking up")
@Examples({"on damage:\n\tif victim is looking up:\n\t\tbroadcast \"it's looking up!\""})
@Since("1.0.3")
@RequiredPlugins("Paper 1.18.2+")
@SuppressWarnings("unused")
public class CondCatIsLookingUp extends PropertyCondition<LivingEntity> {

    static {
        if (PAPER_1_18_2)
            register(CondCatIsLookingUp.class, "looking up", "livingentities");
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