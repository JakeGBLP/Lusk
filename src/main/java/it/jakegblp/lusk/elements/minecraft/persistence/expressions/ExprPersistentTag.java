package it.jakegblp.lusk.elements.minecraft.persistence.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.api.enums.PersistentTagType;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ExprPersistentTag extends PropertyExpression<PersistentDataContainer, Object> {

    static {
        register(ExprPersistentTag.class, Object.class, "%persistenttagtype% %string%", "persistentdatacontainers");
        // todo: fix issues with number classes, add nested tags, improve toString, support PDH ^^^
    }

    private Expression<PersistentTagType> tagTypeExpression;
    private Expression<String> stringExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (LiteralUtils.hasUnparsedLiteral(expressions[0])) {
            setExpr(LiteralUtils.defendExpression(expressions[0]));
            return LiteralUtils.canInitSafely(getExpr());
        }
        if (matchedPattern == 0) {
            tagTypeExpression = (Expression<PersistentTagType>) expressions[0];
            stringExpression = (Expression<String>) expressions[1];
            setExpr((Expression<? extends PersistentDataContainer>) expressions[2]);
        } else {
            setExpr((Expression<? extends PersistentDataContainer>) expressions[0]);
            tagTypeExpression = (Expression<PersistentTagType>) expressions[1];
            stringExpression = (Expression<String>) expressions[2];
        }
        return true;
    }


    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    protected Object[] get(Event event, PersistentDataContainer[] source) {
        String string = stringExpression.getSingle(event);
        if (string == null) return new Object[0];
        PersistentTagType tagType = tagTypeExpression.getSingle(event);
        if (tagType == null) return new Object[0];
        return Arrays.stream(source).map(container -> tagType.get(container,new NamespacedKey(Lusk.getInstance(),string))).toArray();
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, DELETE -> new Class[]{Object.class};
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        String string = stringExpression.getSingle(event);
        if (string == null) return;
        if (mode == Changer.ChangeMode.DELETE) {
            for (PersistentDataContainer persistentDataContainer : getExpr().getArray(event)) {
                persistentDataContainer.remove(new NamespacedKey(Lusk.getInstance(),string));
            }
        } else {
            PersistentTagType tagType = tagTypeExpression.getSingle(event);
            if (tagType == null) return;
            Object o = delta[0];
            for (PersistentDataContainer persistentDataContainer : getExpr().getArray(event)) {
                tagType.set(persistentDataContainer, new NamespacedKey(Lusk.getInstance(),string), o);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return getExpr().toString(event, debug) + "'s persistent " + tagTypeExpression.toString(event, debug) + " tag " + stringExpression.toString(event, debug);
    }
}
