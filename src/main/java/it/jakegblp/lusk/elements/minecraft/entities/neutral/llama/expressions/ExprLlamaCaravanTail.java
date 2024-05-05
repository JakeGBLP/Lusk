package it.jakegblp.lusk.elements.minecraft.entities.neutral.llama.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.jetbrains.annotations.NotNull;

@Name("Llama - Caravan Tail")
@Description("Returns the caravan tail of a llama.\nCan be set.")
@Examples({"broadcast caravan tail of target"})
@Since("1.0.3")
public class ExprLlamaCaravanTail extends SimplePropertyExpression<Entity, Entity> {
    static {
        register(ExprLlamaCaravanTail.class, Entity.class, "llama caravan tail", "entities");
    }

    @Override
    public @NotNull Class<? extends Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public Entity convert(Entity e) {
        if (e instanceof Llama llama) return llama.getCaravanTail();
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "llama caravan tail";
    }
}