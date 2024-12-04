package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Protocol ID")
@Description("Returns the network protocol ID for one or more entities.")
@Examples("broadcast entity id of target")
@Since("1.3")
public class ExprEntityId extends SimplePropertyExpression<Entity,Integer> {

    static {
        register(ExprEntityId.class, Integer.class, "entity id", "entities");
    }

    @Override
    public @Nullable Integer convert(Entity from) {
        return from.getEntityId();
    }

    @Override
    protected String getPropertyName() {
        return "entity id";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}
