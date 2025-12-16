package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.player.MutableProfileProperty;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPlayerProfileProperty extends SimpleExpression<MutableProfileProperty> {

    static {
        Skript.registerExpression(ExprPlayerProfileProperty.class, MutableProfileProperty.class, ExpressionType.COMBINED,
                "[a] [new] [player] profile property named %-string% with value %-string% [and [with] signature %-string%]");
    }

    private Expression<String> nameExpression, valueExpression, signatureExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        nameExpression = (Expression<String>) expressions[0];
        if (nameExpression == null) {
            Skript.error("Missing profile property name.");
            return false;
        }
        valueExpression = (Expression<String>) expressions[1];
        if (valueExpression == null) {
            Skript.error("Missing profile property value.");
            return false;
        }
        signatureExpression = (Expression<String>) expressions[2];
        if (!AddonUtils.initLiteralExpressions(literal -> CommonUtils.isBase64(literal.getSingle()), signatureExpression)) {
            Skript.error("You must provide a valid Base64 signature.");
            return false;
        }
        return true;
    }

    @Override
    protected MutableProfileProperty @Nullable [] get(Event event) {
        String name = nameExpression.getSingle(event);
        if (name == null) return new MutableProfileProperty[0];
        String value = valueExpression.getSingle(event);
        if (value == null) return new MutableProfileProperty[0];
        String signature = AddonUtils.getSingleNullable(signatureExpression, event);
        return new MutableProfileProperty[] {new MutableProfileProperty(name, value, signature)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends MutableProfileProperty> getReturnType() {
        return MutableProfileProperty.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        var builder = new SyntaxStringBuilder(event, debug)
                .append("player profile property named")
                .append(nameExpression)
                .append("with value")
                .append(valueExpression);
        if (signatureExpression != null)
            builder.append("and with signature").append(signatureExpression);
        return builder.toString();
    }
}
