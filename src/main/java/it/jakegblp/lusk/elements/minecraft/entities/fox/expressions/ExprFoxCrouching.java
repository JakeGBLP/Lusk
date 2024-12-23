package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;

@Name("Fox - is Crouching (Property)")
@Description("Gets whether the provided foxes are crouching, can be set and reset.")
@Examples({"broadcast the fox is crouching property of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprFoxCrouching extends SimpleBooleanPropertyExpression<LivingEntity> {
    static {
        register(ExprFoxCrouching.class, Boolean.class, "fox", "[is] crouching","livingentities");
    }

    @Override
    public Boolean convert(LivingEntity from) {
        return from instanceof Fox fox && fox.isCrouching();
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
    public void set(LivingEntity from, Boolean bool) {
        if (from instanceof Fox fox)
            fox.setCrouching(bool);
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    protected String getPropertyName() {
        return "fox is crouching";
    }
}