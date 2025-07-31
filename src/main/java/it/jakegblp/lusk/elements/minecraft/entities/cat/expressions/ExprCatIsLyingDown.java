package it.jakegblp.lusk.elements.minecraft.entities.cat.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Cat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;

@Name("Cat - is Lying Down (Property)")
@Description("Returns whether or not a cat is lying down.\nCan be set and reset.")
@Examples({"broadcast cat is lying down state of target"})
@Since("1.0.3, 1.3 (Plural, Reset)")
@DocumentationId("9143")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprCatIsLyingDown extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_1_18_2)
            register(ExprCatIsLyingDown.class, Boolean.class, "cat", "[is] lying down", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Cat cat && cat.isLyingDown();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Cat cat) {
            cat.setLyingDown(to);
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
        return "cat is lying down";
    }
}