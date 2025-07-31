package it.jakegblp.lusk.elements.minecraft.entities.enderman.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;

@Name("Enderman - Has Been Stared At (Property)")
@Description("Returns whether or not an enderman has been stared at.\nCan be set.")
@Examples({"broadcast has been stared at state of target"})
@Since("1.0.2")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprEndermanBeenStaredAt extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_1_18_2)
            register(ExprEndermanBeenStaredAt.class,Boolean.class,
                    "[enderman]","[has] been (stared|looked) at", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Enderman enderman && enderman.hasBeenStaredAt();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Enderman enderman) {
            enderman.setHasBeenStaredAt(to);
        }
    }

    @Override
    public void reset(LivingEntity from) {
        set(from,false);
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
        return "has been stared at";
    }
}