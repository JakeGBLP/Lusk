package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity Height")
@Description("Returns the Height of an Entity.")
@Examples({"broadcast height of target"})
@Since("1.0.0")
public class ExprHeight extends SimplePropertyExpression<Entity, Double> {
    static {
        register(ExprHeight.class, Double.class, "height", "entity");
    }

    @Override
    public @NotNull Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    @Nullable
    public Double convert(Entity e) {
        if (e != null) {
            return e.getHeight();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "height";
    }
}