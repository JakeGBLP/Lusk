package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions.sounds;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - High Speed Swimming Splash Sound")
@Description("Returns the Sound this entity makes when splashing in water at high speeds. For most entities, this is just 'ENTITY_GENERIC_SPLASH'.")
@Examples({"broadcast high speed swimming splash sound of target"})
@Since("1.0.2")
public class ExprEntityHighSpeedSwimSplashSound extends SimplePropertyExpression<Entity, String> {
    static {
        register(ExprEntityHighSpeedSwimSplashSound.class, String.class, "high speed swim[ming] splash sound", "entities");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(Entity e) {
        return e.getSwimHighSpeedSplashSound().name();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "high speed swimming splash sound";
    }
}