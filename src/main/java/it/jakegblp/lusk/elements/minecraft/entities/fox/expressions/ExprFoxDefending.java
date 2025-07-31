package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;
@Name("Fox - is Defending (Property)")
@Description("Gets whether the provided foxed are defending, can be set and reset.")
@Examples({"broadcast the fox is defending property of target"})
@Since("1.3")
@RequiredPlugins("Paper 1.18.2+")
@SuppressWarnings("unused")
public class ExprFoxDefending extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_1_18_2)
            register(ExprFoxDefending.class, Boolean.class, "fox", "[is] defending","livingentities");
    }

    @Override
    public Boolean convert(LivingEntity from) {
        return from instanceof Fox fox && fox.isDefending();
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
            fox.setDefending(bool);
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    protected String getPropertyName() {
        return "fox is defending";
    }
}