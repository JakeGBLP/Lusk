package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_FOX_API;
import static it.jakegblp.lusk.utils.LuskUtils.registerVerboseBooleanPropertyExpression;

@Name("Fox - is Sleeping (Property)")
@Description("""
        Various fox properties.
        Everything in this expression can be used with `Paper`.
        With `Spigot` you can only:
         - `Get` and `Set` the Crouching State and Sleeping State
         - `Get` the Faceplanted State
        """)
@Examples({"broadcast fox is sleeping property of target"})
@Since("1.2")
@DocumentationId("9076")
@SuppressWarnings("unused")
public class ExprFoxSleeping extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_HAS_FOX_API)
            registerVerboseBooleanPropertyExpression(ExprFoxLeaping.class, Boolean.class, "fox", "[is] sleeping","livingentities");
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Fox fox) {
            fox.setSleeping(to);
        }
    }

    @Override
    public Boolean convert(LivingEntity from) {
        if (from instanceof Fox fox) {
            return fox.isSleeping();
        }
        return false;
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }


    @Override
    protected String getPropertyName() {
        return "fox is sleeping property";
    }
}