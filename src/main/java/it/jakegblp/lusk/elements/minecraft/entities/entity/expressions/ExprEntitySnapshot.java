package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntitySnapshot;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.HAS_ENTITY_SNAPSHOT;

@Name("Entity - Snapshot")
@Description("Creates an EntitySnapshot representing the current state of this entity.")
@Examples({"broadcast entity height of target"})
@Since("1.3")
@RequiredPlugins("1.20.2")
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class ExprEntitySnapshot extends SimplePropertyExpression<Entity, EntitySnapshot> {
    static {
        if (HAS_ENTITY_SNAPSHOT)
            register(ExprEntitySnapshot.class, EntitySnapshot.class, "entity snapshot", "entities");
    }

    @Override
    public @NotNull Class<? extends EntitySnapshot> getReturnType() {
        return EntitySnapshot.class;
    }

    @Override
    public EntitySnapshot convert(Entity e) {
        return e.createSnapshot();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "entity snapshot";
    }
}