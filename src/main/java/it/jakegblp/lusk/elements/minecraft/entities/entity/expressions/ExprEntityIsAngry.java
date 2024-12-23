package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.*;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.isAngry;
import static it.jakegblp.lusk.utils.EntityUtils.setIsAngry;

@Name("Entity - is Angry (Property)")
@Description("""
Returns the Angry Property of an entity. (Warden, PigZombie, Wolf, Enderman)
Can be set for all except wardens.

To use this for endermen `Paper 1.18.2+` is required.
""")
@Examples({"broadcast is angry state of target"})
@Since("1.0.2")
@DocumentationId("9070")
@SuppressWarnings("unused")
public class ExprEntityIsAngry extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        register(ExprEntityIsAngry.class, Boolean.class, "[is] angry", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return isAngry(from);
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        setIsAngry(from, to);
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