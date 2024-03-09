package it.jakegblp.lusk.elements.expressions;

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

@Name("Entity - Death Sound")
@Description("Returns the Sound this entity will make on death.")
@Examples({"broadcast death sound of target"})
@Since("1.0.4")
public class ExprEntityDeathSound extends SimplePropertyExpression<LivingEntity, String> {
    static {
        register(ExprEntityDeathSound.class, String.class, "death sound", "livingentities");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(LivingEntity e) {
        Sound sound = e.getDeathSound();
        if (sound != null) return sound.name();
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "death sound";
    }
}