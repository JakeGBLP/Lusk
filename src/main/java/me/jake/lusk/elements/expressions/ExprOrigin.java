package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Origin")
@Description("Gets the location where this entity originates from.\n" +
        "This value can be null if the entity hasn't yet been added to the world.")
@Examples({"broadcast origin of target"})
@Since("1.0.2")
public class ExprOrigin extends SimplePropertyExpression<Entity, Location> {
    static {
        register(ExprOrigin.class, Location.class, "origin", "entity");
    }

    @Override
    public @NotNull Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    @Nullable
    public Location convert(Entity e) {
        if (e != null) {
            return e.getOrigin();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "origin";
    }
}