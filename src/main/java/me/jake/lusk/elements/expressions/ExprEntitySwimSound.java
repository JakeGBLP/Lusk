package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Swimming Sound")
@Description("Returns the Sound this entity makes while swimming.")
@Examples({"broadcast swimming sound of target"})
@Since("1.0.2")
public class ExprEntitySwimSound extends SimplePropertyExpression<Entity, String> {
    static {
        register(ExprEntitySwimSound.class, String.class, "swim[ming] sound", "entity");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(Entity e) {
        if (e != null) {
            return e.getSwimSound().name();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "swimming sound";
    }
}