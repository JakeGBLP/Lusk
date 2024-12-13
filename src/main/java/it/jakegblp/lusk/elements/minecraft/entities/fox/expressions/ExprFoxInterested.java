package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_FOX_API;
import static it.jakegblp.lusk.utils.LuskUtils.registerVerboseBooleanPropertyExpression;

@Name("Fox - is Interested (Property)")
@Description("Gets whether the provided foxes are interested, can be set and reset.")
@Examples({"broadcast the fox is interested property of target"})
@Since("1.3")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprFoxInterested extends SimpleBooleanPropertyExpression<LivingEntity> {
    static {
        if (PAPER_HAS_FOX_API)
            registerVerboseBooleanPropertyExpression(ExprFoxInterested.class, Boolean.class, "fox", "[is] interested","livingentities");
    }

    @Override
    public Boolean convert(LivingEntity from) {
        if (from instanceof Fox fox)
            return fox.isInterested();
        return false;
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
            fox.setInterested(bool);
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    protected String getPropertyName() {
        return "fox is interested property";
    }
}