package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.LuskUtils.registerVerboseBooleanPropertyExpression;

@Name("Armor Stand - is Invisible (Property)")
@Description("""
Gets and sets the `Invisible` property of an armorstand entity or item, to do so with an armorstand item you must have Paper.
""")
@Examples({"set invisibility property of target to true", "set whether armor stand target is visible to true"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprArmorStandIsInvisible extends SimplePropertyExpression<Object, Boolean> {

    static {
        registerVerboseBooleanPropertyExpression(ExprArmorStandIsInvisible.class, Boolean.class, "[armor[ |-]stand]", "([is] [:in]visible|[:in]visibility)", "livingentities/itemtypes");
    }

    private boolean invisible;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        super.init(expressions, matchedPattern, isDelayed, parseResult);
        invisible = parseResult.hasTag("in");
        return true;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[] {Boolean.class} : null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof Boolean bool) {
            boolean finalBoolean = !invisible ^ bool;
            getExpr().stream(event).forEach(object -> ArmorStandUtils.setIsInvisible(object, finalBoolean));
        }
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return !invisible ^ ArmorStandUtils.isInvisible(from);
    }

    @Override
    protected String getPropertyName() {
        return "the armor stand is "+(invisible ? "invisible" : "visible") + " property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}