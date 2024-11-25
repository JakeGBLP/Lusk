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

import static it.jakegblp.lusk.utils.LuskUtils.registerVerbosePropertyExpression;

@Name("Armor Stand - has Arms (Property)")
@Description("""
Gets and sets the `hasHarms` property of an armorstand entity or item, to do so with an armorstand item you must have Paper.
""")
@Examples({"set has arms property of target to true", "set whether armor stand target has arms to true"})
@Since("1.0.2, 1.3 (item)")
@SuppressWarnings("unused")
public class ExprArmorStandHasArms extends SimplePropertyExpression<Object, Boolean> {

    static {
        registerVerbosePropertyExpression(ExprArmorStandHasArms.class, Boolean.class, "[armor[ |-]stand]", "([have|has|show[s]|should show] [its|their] arms|arms [:in]visibility)", "livingentities/itemtypes");
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
            getExpr().stream(event).forEach(object -> ArmorStandUtils.setHasArms(object, bool));
        }
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return invisible ^ ArmorStandUtils.hasArms(from);
    }

    @Override
    protected String getPropertyName() {
        return "the armor stand has arms property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}