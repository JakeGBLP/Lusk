package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.NotNull;

@Name("Animal - is Interested")
@Description("Checks if an entity is interested.\n(Wolf, Fox)")
@Examples({"on damage of wolf:\n\tif victim is interested:\n\t\tcancel event"})
@Since("1.0.0")
public class CondAnimalInterested extends PropertyCondition<LivingEntity> {
    static {
        register(CondAnimalInterested.class, "interested", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Fox fox) {
            return fox.isInterested();
        } else if (entity instanceof Wolf wolf) {
            return wolf.isInterested();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "interested";
    }
}