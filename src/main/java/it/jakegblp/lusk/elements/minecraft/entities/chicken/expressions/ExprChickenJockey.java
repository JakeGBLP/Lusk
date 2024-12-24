package it.jakegblp.lusk.elements.minecraft.entities.chicken.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_19_2_EXTENDED_ENTITY_API;

@Name("Chicken - is Chicken Jockey (Property)")
@Description("Checks if the provided chickens are chicken jockeys.")
@Examples({"set the is a chicken jockey property of {_chicken} to true"})
@Since("1.3")
@RequiredPlugins("Paper")
public class ExprChickenJockey extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_HAS_1_19_2_EXTENDED_ENTITY_API)
            register(ExprChickenJockey.class,Boolean.class,"[is] [a] chicken jockey","livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Chicken chicken && chicken.isChickenJockey();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Chicken chicken) {
            chicken.setIsChickenJockey(to);
        }
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "is a chicken jockey";
    }
}
