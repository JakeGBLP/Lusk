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

@Name("Armor Stand - has Arms (Property)")
@Description("""
Gets and sets the `hasHarms` property of an armorstand entity or item, to do so with an armorstand item you must be using Paper.
""")
@Examples({"set has arms property of target to true", "set whether armor stand target has arms to true"})
@Since("1.0.2, 1.3 (item)")
@SuppressWarnings("unused")
public class ExprArmorStandHasArms extends SimplePropertyExpression<Object, Boolean> {

    static {
        registerVerboseBooleanPropertyExpression(ExprArmorStandHasArms.class, Boolean.class, "[armor[ |-]stand]", "([have|has|show[s]|should show] [its|their] arms|arms [:in]visibility)", "armorstands/itemtypes");
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
        return switch (mode) {
            case SET -> new Class[]{Boolean.class};
            case RESET, DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        boolean hasArms = false;
        if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof Boolean bool) {
            hasArms = bool ^ invisible;
        }
        for (Object armorStand : getExpr().getAll(event)) {
            ArmorStandUtils.setHasArms(armorStand, hasArms);
        }
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return invisible ^ ArmorStandUtils.hasArms(from);
    }

    @Override
    protected String getPropertyName() {
        return "the armor stand arms "+(invisible ? "invisibility" : "visibility")+" property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}