package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is In Powdered Snow")
@Description("Checks if an entity is in powdered snow.")
@Examples({"if player is in powdered snow:"})
@Since("1.0.2")
public class CondInPowderedSnow extends PropertyCondition<Entity> {
    static {
        register(CondInPowderedSnow.class, "in powdered snow", "entity");
    }

    @Override
    public boolean check(Entity entity) {
        if (entity != null) {
            return entity.isInPowderedSnow();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "in powdered snow";
    }
}