package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions.sounds;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Swimming Sound")
@Description("Returns the Sound this entity makes while swimming.")
@Examples({"broadcast swimming sound of target"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprEntitySwimSound extends SimplePropertyExpression<Entity, Sound> {
    static {
        register(ExprEntitySwimSound.class, Sound.class, "swim[ming] sound", "entity");
    }

    @Override
    public @NotNull Class<? extends Sound> getReturnType() {
        return Sound.class;
    }

    @Override
    @Nullable
    public Sound convert(Entity e) {
        return e.getSwimSound();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "swimming sound";
    }
}