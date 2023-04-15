package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import me.jake.lusk.utils.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sittable;
import org.jetbrains.annotations.NotNull;

@Name("Animal - is Sat")
@Description("Checks if an entity is sitting.\n(Cats, Wolves, Parrots, Pandas and Foxes)")
@Examples({"on damage of wolf, cat or fox:\n\tif victim is sitting:\n\t\tcancel event"})
@Since("1.0.0")
public class CondAnimalSat extends PropertyCondition<LivingEntity> {
    static {
        register(CondAnimalSat.class, "s(at [down]|it[ting [down]])", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity != null) {
            if (Utils.isSittable(EntityData.fromEntity(entity))) {
                return ((Sittable) entity).isSitting();
            }
            Skript.error("You can only use this condition with Cats, Wolves, Parrots, Pandas and Foxes!");
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "sitting";
    }
}
