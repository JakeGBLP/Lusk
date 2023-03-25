package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sittable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
            List<EntityType> sittables = new ArrayList<>() {{
                add(EntityType.CAT);
                add(EntityType.WOLF);
                add(EntityType.PARROT);
                add(EntityType.PANDA);
                add(EntityType.FOX);
            }};
            if (sittables.contains(entity.getType())) {
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
