package it.jakegblp.lusk.elements.minecraft.entities.hostile.warden.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Warden;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Warden - Anger")
@Description("""
        Returns the anger of a warden.
                
        Angry = 80-150
        Agitated = 40-79
        Calm = 0-39
                
        Use the respective conditions to check for those 3 states.""")
@Examples({"broadcast anger of target"})
@Since("1.0.2")
public class ExprWardenAnger extends SimplePropertyExpression<Entity, Integer> {
    static {
        register(ExprWardenAnger.class, Integer.class, "anger", "entity");
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    @Nullable
    public Integer convert(Entity e) {
        if (e instanceof Warden warden) {
            return warden.getAnger();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "anger";
    }
}