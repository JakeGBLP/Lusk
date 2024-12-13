package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.SkriptParser;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PAPER_TYPES;
import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;

@Name("Armor Stand - is Invisible (Property)")
@Description("""
Gets and sets the `Invisible` property of an armorstand entity or item, to do so with an armorstand item you must be using Paper.
""")
@Examples({"set invisibility property of target to true", "set whether armor stand target is visible to true"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprArmorStandIsInvisible extends SimpleBooleanPropertyExpression<Object> {

    static {
        register(ExprArmorStandIsInvisible.class, Boolean.class, ARMOR_STAND_PREFIX, "([is] [:in]visible|[:in]visibility)", ARMOR_STAND_PAPER_TYPES);
    }

    @Override
    public boolean setNegated(int matchedPattern, SkriptParser.ParseResult parseResult) {
        return parseResult.hasTag("in");
    }

    @Override
    public void set(Object from, Boolean to) {
        ArmorStandUtils.setIsInvisible(from, to);
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
        return ArmorStandUtils.isInvisible(from) ^ !isNegated();
    }

    @Override
    protected String getPropertyName() {
        return "the armor stand is "+(isNegated() ? "in" : "") +"visible property";
    }
}