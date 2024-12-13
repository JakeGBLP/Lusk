package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_FOX_API;
import static it.jakegblp.lusk.utils.LuskUtils.registerVerboseBooleanPropertyExpression;

@Name("Fox - is Leaping (Property)")
@Description("Gets whether the provided foxed are leaping, can be set and reset.")
@Examples({"broadcast the fox is leaping property of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprFoxLeaping extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_HAS_FOX_API)
            registerVerboseBooleanPropertyExpression(ExprFoxLeaping.class, Boolean.class, "fox", "[is] leaping","livingentities");
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
        if (from instanceof Fox fox) {
            return fox.isLeaping();
        }
        return false;
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
        return "fox is leaping property";
    }
}