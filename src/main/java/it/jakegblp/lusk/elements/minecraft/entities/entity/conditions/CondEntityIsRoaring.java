package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.EntityUtils.isRoaring;

@Name("Entity - is Roaring")
@Description("Checks if an entity is roaring.\nFor ravagers, Paper 1.19.2+ is required.")
@Examples("on damage of ravager:\n\tvictim is roaring\n\tkill victim")
@Since("1.1.1")
public class CondEntityIsRoaring extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityIsRoaring.class, "roaring", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return isRoaring(entity);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "roaring";
    }
}