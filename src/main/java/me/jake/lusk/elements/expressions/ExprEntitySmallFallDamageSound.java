package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Small Fall Damage Sound")
@Description("Returns the Sound this entity will make when falling from a small height.")
@Examples({"broadcast small fall damage sound of target"})
@Since("1.0.4")
public class ExprEntitySmallFallDamageSound extends SimplePropertyExpression<Entity, String> {
    static {
        register(ExprEntitySmallFallDamageSound.class, String.class, "small fall damage sound", "entity");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(Entity e) {
        if (e instanceof LivingEntity entity) {
            return entity.getFallDamageSoundSmall().name();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "small fall damage sound";
    }
}