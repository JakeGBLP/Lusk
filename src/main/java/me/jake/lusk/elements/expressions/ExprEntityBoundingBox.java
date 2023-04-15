package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bounding Box - Entity")
@Description("Returns the entity's bounding box which reflects the entity's location and size.")
@Examples({"broadcast bounding box of target"})
@Since("1.0.2")
public class ExprEntityBoundingBox extends SimplePropertyExpression<Entity, BoundingBox> {
    static {
        register(ExprEntityBoundingBox.class, BoundingBox.class, "bounding box", "entity");
    }

    @Override
    public @NotNull Class<? extends BoundingBox> getReturnType() {
        return BoundingBox.class;
    }

    @Override
    @Nullable
    public BoundingBox convert(Entity e) {
        if (e != null) {
            return e.getBoundingBox();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "bounding box";
    }
}