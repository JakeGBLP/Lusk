package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - Height")
@Description("Returns the Height of an Entity.")
@Examples({"broadcast height of target"})
@Since("1.0.0")
@SuppressWarnings("unused")
public class ExprEntityHeight extends SimplePropertyExpression<Entity, Double> {
    static {
        register(ExprEntityHeight.class, Double.class, "height", "entity");
    }

    @Override
    public @NotNull Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    @NotNull
    public Double convert(Entity e) {
        return e.getHeight();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "height";
    }
}