package it.jakegblp.lusk.elements.minecraft.entities.dolphin.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_18_2_EXTENDED_ENTITY_API;

@Name("Dolphin - Has Been Fish (Property)")
@Description("Returns whether or not the provided dolphins have been fed a fish.\nCan be set.")
@Examples({"broadcast has fish state of target"})
@Since("1.0.3, 1.3 (Plural)")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprDolphinHasFishState extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API)
            register(ExprDolphinHasFishState.class, Boolean.class, "[dolphin]", "[has] been fed [a] fish", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Dolphin dolphin && dolphin.hasFish();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Dolphin dolphin) {
            dolphin.setHasFish(to);
        }
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "has been fed a fish";
    }
}