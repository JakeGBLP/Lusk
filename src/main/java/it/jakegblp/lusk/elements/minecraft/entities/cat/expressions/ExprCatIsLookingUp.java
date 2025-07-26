package it.jakegblp.lusk.elements.minecraft.entities.cat.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Cat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_18_2_EXTENDED_ENTITY_API;

@Name("Cat - is Looking Up (Property)")
@Description("Returns whether or not a cat is looking up.\nCan be set and reset.")
@Examples({"broadcast cat looking up state of target"})
@Since("1.0.3, 1.3 (Plural, Reset)")
@DocumentationId("11897")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprCatIsLookingUp extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API)
            register(ExprCatIsLookingUp.class, Boolean.class, "cat", "[is] looking up", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Cat cat && cat.isHeadUp();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Cat cat) {
            cat.setHeadUp(to);
        }
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
        return "cat is looking up";
    }
}