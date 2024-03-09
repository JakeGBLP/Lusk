package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Swimming Splash Sound")
@Description("Returns the Sound this entity makes when splashing in water. For most entities, this is just 'ENTITY_GENERIC_SPLASH'.")
@Examples({"broadcast swimming splash sound of target"})
@Since("1.0.2")
public class ExprEntitySwimSplashSound extends SimplePropertyExpression<Entity, String> {
    static {
        register(ExprEntitySwimSplashSound.class, String.class, "swim[ming] splash sound", "entity");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(Entity e) {
        return e.getSwimSplashSound().name();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "swimming splash sound";
    }
}