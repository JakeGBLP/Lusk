package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is From a Mob Spawner")
@Description("Checks whether this entity was spawned from a mob spawner.")
@Examples({"on right click:\n\tif entity is from a spawner:\n\t\tbroadcast \"you clicked a mob that has spawned from a spawner!\""})
@Since("1.0.2")
@DocumentationId("9040")
public class CondEntityFromMobSpawner extends PropertyCondition<Entity> {
    static {
        register(CondEntityFromMobSpawner.class, "([a] spawner entit(y|ies)|from [a] [mob] spawner)", "entities");
    }

    @Override
    public boolean check(Entity entity) {
        return entity.fromMobSpawner();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "from mob spawner";
    }
}