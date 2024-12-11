package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - Width")
@Description("Returns the Width of an Entity.")
@Examples({"broadcast entity width of target"})
@Since("1.0.2, 1.3 (Plural)")
@SuppressWarnings("unused")
public class ExprEntityWidth extends SimplePropertyExpression<Entity, Double> {
    static {
        register(ExprEntityWidth.class, Double.class, "entity width", "entities");
    }

    @Override
    public @NotNull Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    @NotNull
    public Double convert(Entity e) {
        return e.getWidth();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "entity width";
    }
}