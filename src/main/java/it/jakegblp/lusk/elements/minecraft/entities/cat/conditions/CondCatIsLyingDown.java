package it.jakegblp.lusk.elements.minecraft.entities.cat.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Cat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;

@Name("Cat - is Lying Down")
@Description("Checks if a cat is laying down.")
@Examples({"on damage of cat:\n\tif victim is laying down:\n\t\tcancel event"})
@Since("1.0.3")
@RequiredPlugins("Paper 1.18.2+")
@SuppressWarnings("unused")
public class CondCatIsLyingDown extends PropertyCondition<LivingEntity> {

    static {
        if (PAPER_1_18_2)
            register(CondCatIsLyingDown.class, "lying down", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Cat cat && cat.isLyingDown();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "lying down";
    }
}