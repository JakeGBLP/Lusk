package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.NotNull;

@Name("is Leaping")
@Description("Checks if an entity is interested.\n(Wolf, Fox)")
@Examples({"on damage of wolf:\n\tif victim is interested:\n\t\tcancel event"})
@Since("1.0.0")
public class CondInterested extends PropertyCondition<LivingEntity> {

    static {
        register(CondInterested.class, "interested", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity != null) {
            if (entity.getType() == EntityType.FOX) {
                return ((Fox) entity).isInterested();
            } else if (entity.getType() == EntityType.WOLF) {
                return ((Wolf) entity).isInterested();
            } else {
                Skript.error("You can only use this condition with Foxes and Wolves!");
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "interested";
    }
}