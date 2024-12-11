package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.SpawnCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.HAS_SPAWN_CATEGORY;

@Name("Entity - Spawn Category")
@Description("Returns the category of spawn to which this entity belongs.")
@Examples({"broadcast spawn category of target"})
@Since("1.0.2, 1.3 (Plural)")
public class ExprEntitySpawnCategory extends SimplePropertyExpression<Entity, SpawnCategory> {
    static {
        if (HAS_SPAWN_CATEGORY)
            register(ExprEntitySpawnCategory.class, SpawnCategory.class, "spawn category", "entities");
    }

    @Override
    public @NotNull Class<? extends SpawnCategory> getReturnType() {
        return SpawnCategory.class;
    }

    @Override
    @Nullable
    public SpawnCategory convert(Entity e) {
        return e.getSpawnCategory();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "spawn category";
    }
}