package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.player.MutableProfileProperty;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;

public class ExprSecPlayerProfileProperty extends SectionExpression<MutableProfileProperty> {

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("name", null, false, String.class))
                .addEntryData(new ExpressionEntryData<>("value", null, false, String.class))
                .addEntryData(new ExpressionEntryData<>("signature", null, true, String.class))
                .build();
        Skript.registerExpression(ExprSecPlayerProfileProperty.class, MutableProfileProperty.class, ExpressionType.COMBINED,
                "[a] new [player] profile property");
    }

    private Expression<String> nameExpression, valueExpression, signatureExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        nameExpression = (Expression<String>) container.getOptional("name", false);
        if (nameExpression == null) {
            Skript.error("Missing profile property name.");
            return false;
        }
        valueExpression = (Expression<String>) container.getOptional("value", false);
        if (valueExpression == null) {
            Skript.error("Missing profile property value.");
            return false;
        }
        signatureExpression = (Expression<String>) container.getOptional("signature", false);
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
        return "a new player profile property";
    }

}
