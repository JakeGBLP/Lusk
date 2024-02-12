package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("is Spawner Entity")
@Description("Checks whether this entity was spawned from a mob spawner.")
@Examples({"on right click:\n\tif entity is a spawner entity:\n\t\tbroadcast \"you clicked a mob that has spawned from a spawner!\""})
@Since("1.0.2")
public class CondSpawnerEntity extends PropertyCondition<Entity> {
    static {
        register(CondSpawnerEntity.class, "[a] spawner entity", "entity");
    }

    @Override
    public boolean check(Entity entity) {
        return entity.fromMobSpawner();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "a spawner entity";
    }
}