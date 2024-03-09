package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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