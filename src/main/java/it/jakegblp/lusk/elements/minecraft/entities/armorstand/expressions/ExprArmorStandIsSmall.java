package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;
import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_TYPES;

@Name("Armor Stand - is Small (Property)")
@Description("""
Gets and sets the `small` property of an armorstand entity or item, to do so with an armorstand item you must have Paper.
""")
@Examples({"set small property of target to true", "set whether armor stand target is small to true"})
@Since("1.0.2, 1.3 (Item)")
@DocumentationId("9059")
@SuppressWarnings("unused")
public class ExprArmorStandIsSmall extends SimpleBooleanPropertyExpression<Object> {

    static {
        register(ExprArmorStandIsSmall.class, Boolean.class, ARMOR_STAND_PREFIX, "[is] small", ARMOR_STAND_TYPES);
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
    public void set(Object from, Boolean to) {
        ArmorStandUtils.setIsSmall(from, to);
    }

    @Override
    public void reset(Object from) {
        set(from, false);
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return ArmorStandUtils.isSmall(from);
    }

    @Override
    protected String getPropertyName() {
        return "armor stand is small property";
    }
}