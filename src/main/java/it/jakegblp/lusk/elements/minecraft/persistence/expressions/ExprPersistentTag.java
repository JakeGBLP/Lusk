package it.jakegblp.lusk.elements.minecraft.persistence.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.PDCApi;
import it.jakegblp.lusk.api.enums.PersistentTagType;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Name("Persistence - Persistent Tag of X")
@Description("""
Get, Set and Delete the value of the specified namespaced key and type, supports getting nested keys using semicolons as delimiters.

""")// todo: finish docs
public class ExprPersistentTag extends PropertyExpression<Object, Object> {

    static {
        register(ExprPersistentTag.class, Object.class, "%persistenttagtype% %string%", "persistentdatacontainers/persistentdataholders");
        // todo: improve toString, add number class char suffix for arrays, add nbt readable and pretty print
    }

    private Expression<PersistentTagType> tagTypeExpression;
    private Expression<String> stringExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            tagTypeExpression = (Expression<PersistentTagType>) expressions[0];
            stringExpression = (Expression<String>) expressions[1];
            setExpr(expressions[2]);
        } else {
            setExpr(expressions[0]);
            tagTypeExpression = (Expression<PersistentTagType>) expressions[1];
            stringExpression = (Expression<String>) expressions[2];
        }
        return true;
    }


    @Override
    public Class<?> getReturnType() {
        return tagTypeExpression instanceof Literal<PersistentTagType> literal ? literal.getSingle().getComplexClass() : Object.class;
    }

    @Override
    public boolean isSingle() {
        return super.isSingle() || tagTypeExpression instanceof Literal<PersistentTagType> literal && literal.getSingle().getComplexClass().isArray();
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
            else return null;
            //Object value = PDCApi.getTag(container, tagType, string);
            return Stream.ofNullable(PDCApi.getTag(container, tagType, string));
            //return Stream.of(tagType.get(container, new NamespacedKey(Lusk.getInstance(), string)));
        }).filter(Objects::nonNull).toArray();
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, DELETE -> new Class[]{tagTypeExpression instanceof Literal<PersistentTagType> literal ? literal.getSingle().getComplexClass() : Object.class};
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        String string = stringExpression.getSingle(event);
        if (string == null) return;
        PersistentDataContainer[] containers = getExpr().stream(event).map(o -> {
            if (o instanceof PersistentDataContainer container) return container;
            else if (o instanceof PersistentDataHolder holder) return holder.getPersistentDataContainer();
            return null;
        }).filter(Objects::nonNull).toArray(PersistentDataContainer[]::new);

        if (mode == Changer.ChangeMode.DELETE) {
            for (PersistentDataContainer container : containers) {
                PDCApi.deleteTag(container, string);
            }
        } else {
            PersistentTagType tagType = tagTypeExpression.getSingle(event);
            if (tagType == null) return;
            for (PersistentDataContainer container : containers) {
                PDCApi.setTag(container, tagType, string, delta);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return getExpr().toString(event, debug) + "'s " + tagTypeExpression.toString(event, debug) + " " + stringExpression.toString(event, debug);
    }
}
