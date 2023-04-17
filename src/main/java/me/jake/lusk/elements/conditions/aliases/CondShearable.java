package me.jake.lusk.elements.conditions.aliases;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import me.jake.lusk.utils.Utils;
import org.jetbrains.annotations.NotNull;

@Name("is Shearable")
@Description("Checks if an entity can normally be sheared. (This condition will pass even if the entity is not ready to be sheared)")
@Examples("if target is shearable:")
@Since("1.0.2")
public class CondShearable extends PropertyCondition<Object> {

    static {
        register(CondShearable.class, "shearable", "entitydata");
    }

    @Override
    public boolean check(Object o) {
        if (o != null) {
            if (o instanceof EntityData<?> entityData) {
                return Utils.isShearable(entityData);
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "shearable";
    }
}
