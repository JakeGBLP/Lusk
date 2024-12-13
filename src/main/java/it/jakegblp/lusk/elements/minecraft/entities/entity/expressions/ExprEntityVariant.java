package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.getVariant;
import static it.jakegblp.lusk.utils.EntityUtils.setVariant;

@Name("Entity - Variant")
@Description("""
        Returns the Variant of an Entity.
        Can be set.
        
        Currently supports:
        - Mushroom Cow
        - Parrot
        - Llama
        - Fox
        - Cat
        - Rabbit
        - Panda
        - Tropical Fish
        - Frog
        - Wolf
        - Salmon
        - Axolotl
        """)
@Examples({"broadcast variant of event-entity","set variant of target to \"red\""})
@Since("1.0.0, 1.0.2 (Set), 1.0.3 (String + More)")
@SuppressWarnings({"unused", "ConstantConditions"})
public class ExprEntityVariant extends SimplerPropertyExpression<LivingEntity,Object> {

    static {
        register(ExprEntityVariant.class, Object.class, "[entity] variant", "livingentities");
    }

    @Override
    public @Nullable Object convert(LivingEntity from) {
        return getVariant(from);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(LivingEntity from, Object to) {
        setVariant(from,to);
    }

    @Override
    protected String getPropertyName() {
        return "entity variant";
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }
}