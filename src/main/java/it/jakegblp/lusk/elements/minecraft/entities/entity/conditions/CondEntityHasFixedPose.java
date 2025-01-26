package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Entity;

@Name("Entity - has Fixed Pose")
@Description("""
Checks whether the provided entities have a fixed pose.""")
@Examples({"if target has a fixed pose:"})
@Since("1.3.3")
@RequiredPlugins("Paper 1.20.1+")
public class CondEntityHasFixedPose extends PropertyCondition<Entity> {

    static {
        register(CondEntityHasFixedPose.class, PropertyType.HAVE, "[a] fixed pose", "entities");
    }

    @Override
    public boolean check(Entity value) {
        return value.hasFixedPose();
    }

    @Override
    protected String getPropertyName() {
        return "a fixed pose";
    }
}
