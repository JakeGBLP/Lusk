package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Width")
@Description("Returns the Width of an Entity.")
@Examples({"broadcast width of target"})
@Since("1.0.2")
public class ExprEntityWidth extends SimplePropertyExpression<Entity, Double> {
    static {
        register(ExprEntityWidth.class, Double.class, "entity width", "entity");
    }

    @Override
    public @NotNull Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    @Nullable
    public Double convert(Entity e) {
        return e.getWidth();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "width";
    }
}