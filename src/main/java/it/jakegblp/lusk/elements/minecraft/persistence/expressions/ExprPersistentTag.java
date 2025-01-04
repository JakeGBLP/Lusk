package it.jakegblp.lusk.elements.minecraft.persistence.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.api.enums.PersistentTagType;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static it.jakegblp.lusk.utils.LuskUtils.consoleLog;

public class ExprPersistentTag extends PropertyExpression<Object, Object> {

    static {
        register(ExprPersistentTag.class, Object.class, "%persistenttagtype% %string%", "persistentdatacontainers/persistentdataholders");
        // todo: add nested tags, improve toString
    }

    private Literal<PersistentTagType> tagTypeLiteral;
    private Expression<PersistentTagType> tagTypeExpression;
    private Expression<String> stringExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            tagTypeExpression = (Expression<PersistentTagType>) expressions[0];
            stringExpression = (Expression<String>) expressions[1];
            if (expressions[2] instanceof Literal<?>) {
                tagTypeLiteral = (Literal<PersistentTagType>) expressions[2];
            }
            setExpr(expressions[2]);
        } else {
            if (expressions[0] instanceof Literal<?>) {
                tagTypeLiteral = (Literal<PersistentTagType>) expressions[0];
            }
            setExpr(expressions[0]);
            tagTypeExpression = (Expression<PersistentTagType>) expressions[1];
            stringExpression = (Expression<String>) expressions[2];
        }
        return true;
    }


    @Override
    public Class<?> getReturnType() {
        return tagTypeLiteral != null ? tagTypeLiteral.getSingle().getDataType().getComplexType() : Object.class;
    }

    @Override
    public boolean isSingle() {
        return super.isSingle() || (tagTypeLiteral != null && tagTypeLiteral.getSingle().getDataType().getComplexType().isArray());
    }

    @Override
    protected Object[] get(Event event, Object[] source) {
        String string = stringExpression.getSingle(event);
        if (string == null) return new Object[0];
        PersistentTagType tagType = tagTypeExpression.getSingle(event);
        if (tagType == null) return new Object[0];
        return Arrays.stream(source).flatMap(object -> {
            PersistentDataContainer container;
            if (object instanceof PersistentDataContainer c) container = c;
            else if (object instanceof PersistentDataHolder holder) container = holder.getPersistentDataContainer();
            else return Stream.of();
            return Stream.of(tagType.get(container, new NamespacedKey(Lusk.getInstance(), string)));
        }).filter(Objects::nonNull).toArray();
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, DELETE -> new Class[]{tagTypeLiteral != null ? tagTypeLiteral.getSingle().getDataType().getComplexType() : Object.class};
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        String string = stringExpression.getSingle(event);
        if (string == null) return;
        PersistentDataContainer[] containers = getExpr().stream(event).map(o -> {
            consoleLog("1: {0}",o instanceof PersistentDataContainer);
            consoleLog("2: {0}",o instanceof PersistentDataHolder);
            if (o instanceof PersistentDataContainer container) return container;
            else if (o instanceof PersistentDataHolder holder) return holder.getPersistentDataContainer();
            return null;
        }).filter(Objects::nonNull).toArray(PersistentDataContainer[]::new);

        if (mode == Changer.ChangeMode.DELETE) {
            for (PersistentDataContainer persistentDataContainer : containers) {
                persistentDataContainer.remove(new NamespacedKey(Lusk.getInstance(),string));
            }
        } else {
            PersistentTagType tagType = tagTypeExpression.getSingle(event);
            if (tagType == null) return;
            Object object = delta[0];
            if (object instanceof Number number) {
                object = switch (tagType) {
                    case BYTE -> number.byteValue();
                    case SHORT -> number.shortValue();
                    case INTEGER -> number.intValue();
                    case LONG -> number.longValue();
                    case FLOAT -> number.floatValue();
                    case DOUBLE -> number.doubleValue();
                    default -> object;
                };
            }
            for (PersistentDataContainer persistentDataContainer : containers) {
                tagType.set(persistentDataContainer, new NamespacedKey(Lusk.getInstance(),string), object);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return getExpr().toString(event, debug) + "'s persistent " + tagTypeExpression.toString(event, debug) + " tag " + stringExpression.toString(event, debug);
    }
}
