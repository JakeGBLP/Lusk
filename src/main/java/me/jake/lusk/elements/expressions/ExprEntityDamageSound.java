package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Damage Sound")
@Description("Returns the Sound this entity will make on damage.")
@Examples({"broadcast damage sound of target"})
@Since("1.0.4")
public class ExprEntityDamageSound extends SimplePropertyExpression<Entity, String> {
    static {
        register(ExprEntityDamageSound.class, String.class, "damage sound", "entity");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(Entity e) {
        if (e instanceof LivingEntity entity) {
            Sound sound = entity.getHurtSound();
            assert sound != null;
            return sound.name();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "damage sound";
    }
}