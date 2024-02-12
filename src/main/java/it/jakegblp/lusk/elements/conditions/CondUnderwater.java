package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Underwater")
@Description("Checks if an entity is underwater.")
@Examples({"if player is underwater:"})
@Since("1.0.2")
public class CondUnderwater extends PropertyCondition<Entity> {

    static {
        register(CondUnderwater.class, "underwater", "entity");
    }

    @Override
    public boolean check(Entity entity) {
        if (entity != null) {
            return entity.isUnderWater();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "underwater";
    }
}