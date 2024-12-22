package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.EntityUtils.isScreaming;

@Name("Entity - is Screaming")
@Description("Checks if an entity is screaming.\n(Goat, Enderman when using Paper 1.18.2+)")
@Examples({"on damage of enderman:\n\tif victim is screaming:\n\t\tcancel event"})
@Since("1.0.2")
public class CondEntityIsScreaming extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityIsScreaming.class, "screaming", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return isScreaming(entity);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "screaming";
    }
}