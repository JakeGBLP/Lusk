package it.jakegblp.lusk.elements.minecraft.entities.neutral.llama.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.jetbrains.annotations.NotNull;

@Name("Llama - Caravan Head")
@Description("Returns the caravan head of a llama.\nCan be set.")
@Examples({"broadcast caravan head of target"})
@Since("1.0.3")
public class ExprLlamaCaravanHead extends SimplePropertyExpression<Entity, Entity> {
    static {
        register(ExprLlamaCaravanHead.class, Entity.class, "llama caravan head", "entities");
    }

    @Override
    public @NotNull Class<? extends Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public Entity convert(Entity e) {
        if (e instanceof Llama llama) return llama.getCaravanHead();
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "llama caravan head";
    }
}