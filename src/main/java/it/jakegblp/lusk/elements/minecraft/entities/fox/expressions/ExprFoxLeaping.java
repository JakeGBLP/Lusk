package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_18_2_EXTENDED_ENTITY_API;

@Name("Fox - is Leaping (Property)")
@Description("Gets whether the provided foxed are leaping, can be set and reset.")
@Examples({"broadcast the fox is leaping property of target"})
@Since("1.3")
@RequiredPlugins("Paper 1.18.2+")
@SuppressWarnings("unused")
public class ExprFoxLeaping extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API)
            register(ExprFoxLeaping.class, Boolean.class, "fox", "[is] leaping","livingentities");
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
    public Boolean convert(LivingEntity from) {
        return from instanceof Fox fox && fox.isLeaping();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Fox fox) {
            fox.setLeaping(to);
        }
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    protected String getPropertyName() {
        return "fox is leaping";
    }
}