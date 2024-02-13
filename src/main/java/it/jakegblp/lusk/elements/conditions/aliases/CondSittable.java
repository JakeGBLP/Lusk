package it.jakegblp.lusk.elements.conditions.aliases;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import it.jakegblp.lusk.utils.Constants;
import org.jetbrains.annotations.NotNull;

@Name("is Sittable")
@Description("Checks if an entity can normally sit.")
@Examples("if target is sittable:")
@Since("1.0.2")
public class CondSittable extends PropertyCondition<Object> {

    static {
        register(CondSittable.class, "sittable", "entitydata");
    }

    @Override
    public boolean check(Object o) {
        if (o != null) {
            if (o instanceof EntityData<?> entityData) {
                return Constants.sittables.contains(entityData);
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "sittable";
    }
}
