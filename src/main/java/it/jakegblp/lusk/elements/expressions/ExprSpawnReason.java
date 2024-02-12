package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Spawn Reason")
@Description("Gets the spawn reason that initially spawned this entity.")
@Examples({"broadcast spawn reason of target"})
@Since("1.0.2")
public class ExprSpawnReason extends SimplePropertyExpression<Entity, CreatureSpawnEvent.SpawnReason> {
    static {
        register(ExprSpawnReason.class, CreatureSpawnEvent.SpawnReason.class, "spawn reason", "entity");
    }

    @Override
    public @NotNull Class<? extends CreatureSpawnEvent.SpawnReason> getReturnType() {
        return CreatureSpawnEvent.SpawnReason.class;
    }

    @Override
    @Nullable
    public CreatureSpawnEvent.SpawnReason convert(Entity e) {
        if (e != null) {
            return e.getEntitySpawnReason();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "spawn reason";
    }
}