package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;
import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_TYPES;

@Name("Armor Stand - is Marker (Property)")
@Description("""
Gets and sets the `marker` property of an armorstand entity or item, to do so with an armorstand item you must have Paper.
""")
@Examples({"set marker of target to true"})
@Since("1.0.2, 1.3 (Item)")
@SuppressWarnings("unused")
public class ExprArmorStandIsMarker extends SimpleBooleanPropertyExpression<Object> {

    static {
        register(ExprArmorStandIsMarker.class, Boolean.class, ARMOR_STAND_PREFIX, "[is] marker", ARMOR_STAND_TYPES);
    }

    @Override
    public void set(Object from, Boolean to) {
        ArmorStandUtils.setIsMarker(from, to);
    }

    @Override
    public void reset(Object from) {
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
    public @Nullable Boolean convert(Object from) {
        return ArmorStandUtils.isMarker(from);
    }

    @Override
    protected String getPropertyName() {
        return "armor stand is marker property";
    }
}