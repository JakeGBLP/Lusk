package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Cat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Cat - is Lying Down")
@Description("Checks if a cat is laying down.")
@Examples({"on damage of cat:\n\tif victim is laying down:\n\t\tcancel event"})
@Since("1.0.3")
public class CondCatIsLyingDown extends PropertyCondition<LivingEntity> {
    static {
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