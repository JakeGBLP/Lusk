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

@Name("Fox - is Faceplanted (Property)")
@Description("Gets whether the provided foxes are faceplanted, if running Paper this can be set and reset.")
@Examples({"broadcast the fox is faceplanted property of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprFoxFaceplanted extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        registerVerboseBooleanPropertyExpression(ExprFoxFaceplanted.class, Boolean.class, "fox", "[is] face[ |-]planted","livingentities");
    }

    @Override
    protected String getPropertyName() {
        return "fox is faceplanted property";
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Fox fox) {
            fox.setFaceplanted(to);
        }
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    public Boolean convert(LivingEntity from) {
        if (from instanceof Fox fox) {
            return fox.isFaceplanted();
        }
        return false;
    }

    @Override
    public boolean allowSet() {
        return PAPER_HAS_FOX_API;
    }

    @Override
    public boolean allowReset() {
        return PAPER_HAS_FOX_API;
    }
}