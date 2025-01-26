package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.SkriptParser;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_TYPES;
import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;

@Name("Armor Stand - has Arms (Property)")
@Description("""
Gets and sets the `hasHarms` property of an armorstand entity or item, to do so with an armorstand item you must be using Paper.
""")
@Examples({"set has arms property of target to true", "set whether armor stand target has arms to true"})
@Since("1.0.2, 1.3 (Item)")
@SuppressWarnings("unused")
public class ExprArmorStandHasArms extends SimpleBooleanPropertyExpression<Object> {

    static {
        register(ExprArmorStandHasArms.class, Boolean.class, ARMOR_STAND_PREFIX, "((have|has|show[s]) [its|their] arms|[have|has] arms [:in]visibility)", ARMOR_STAND_TYPES);
    }

    @Override
    public boolean setNegated(int matchedPattern, SkriptParser.ParseResult parseResult) {
        return parseResult.hasTag("in");
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
        ArmorStandUtils.setHasArms(from, to);
    }

    @Override
    public void reset(Object from) {
        ArmorStandUtils.setHasArms(from, false);
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return isNegated() ^ ArmorStandUtils.hasArms(from);
    }

    @Override
    protected String getPropertyName() {
        return "armor stand arms "+(isNegated() ? "in" : "") + "visibility property";
    }
}