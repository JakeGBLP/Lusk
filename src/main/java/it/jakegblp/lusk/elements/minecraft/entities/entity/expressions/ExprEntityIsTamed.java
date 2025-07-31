package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.Nullable;

@Name("Entity - is Tamed (Property)")
@Description("""
Returns whether the provided entities are tamed.
Can be set.

If tamed, an entity cannot be tamed by a player through normal methods, even if it does not belong to anyone in particular.
""")
@Examples({"broadcast is tamed state of target"})
@Since("1.3.8")
@SuppressWarnings("unused")
public class ExprEntityIsTamed extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        register(ExprEntityIsTamed.class, Boolean.class, "tameable", "[is] tamed", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Tameable tameable && tameable.isTamed();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Tameable tameable) {
            tameable.setTamed(to);
        }
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "is angry";
    }
}