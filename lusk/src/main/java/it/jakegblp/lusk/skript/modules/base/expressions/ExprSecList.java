package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.reflection.SimpleArray;
import it.jakegblp.lusk.skript.api.syntax.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.syntax.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.*;

import static it.jakegblp.lusk.skript.utils.AddonUtils.errorForNode;
import static it.jakegblp.lusk.skript.utils.AddonUtils.parseSectionNodes;

public class ExprSecList extends SectionExpression<Object> implements AsyncableSyntaxesWrapper {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprSecList.class, ExprSecList::new, Object.class,
                "[a[n]] [%-classinfos%] list [of|containing]");
    }

    private ExpressionList<?> objectExpressionList;
    private Expression<ClassInfo<?>> classInfoExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        if (node == null) {
            Skript.error("Empty list section expression.");
            return false;
        }
        classInfoExpression = (Expression<ClassInfo<?>>) expressions[0];
        List<Class<?>> allowedClasses = new ArrayList<>();
        boolean justObject = false;
        for (Literal<? extends ClassInfo<?>> literal : AddonUtils.getLiterals(classInfoExpression)) {
            for (ClassInfo<?> classInfo : literal.getAll()) {
                Class<?> type = classInfo.getC();
                if (type == Object.class) {
                    justObject = true;
                    break;
                }
                allowedClasses.add(type);
            }
        }
        if (justObject || allowedClasses.isEmpty())
            allowedClasses = List.of(Object.class);

        Class<?>[] allowedClassesArray = allowedClasses.toArray(new Class[0]);
        var sectionParseResult = parseSectionNodes(this.getAsSection(), node, Object.class, (subNode, key) ->
                errorForNode(subNode, key + " is " + SkriptParser.notOfType(allowedClassesArray)), allowedClasses.toArray(Class[]::new));
        if (sectionParseResult.hasFailed()) {
            Skript.error("Parsed empty list section expression.");
            return false;
        }
        objectExpressionList = (ExpressionList<?>) LiteralUtils.defendExpression(sectionParseResult.expressionList());
        if (objectExpressionList == null) {
            Skript.error("Something went wrong.");
            return false;
        }
        return LiteralUtils.canInitSafely(objectExpressionList);
    }

    @Override
    public boolean isSectionOnly() {
        return true;
    }

    @Override
    @SuppressWarnings("all")
    protected Object @Nullable [] get(Event event) {
        return Arrays.stream(objectExpressionList.getArray(event))
                .filter(object -> {
                    for (Class<?> possibleReturnType : possibleReturnTypes())
                        if (possibleReturnType.isInstance(object))
                            return true;
                    return false;
                }).toArray(size -> SimpleArray.create(getReturnType(), size));
    }


    @Override
    public boolean isSingle() {
        return objectExpressionList.isSingle();
    }

    @Override
    public Class<?> getReturnType() {
        Class<?>[] allowed = possibleReturnTypes();
        Class<?> baseType = objectExpressionList.getReturnType();
        if (allowed.length == 0) return baseType;
        Class<?> candidate = objectExpressionList.getReturnType();
        while (candidate != null) {
            Class<?> finalCandidate = candidate;
            boolean valid = Arrays.stream(allowed).allMatch(allowedType -> finalCandidate.isAssignableFrom(allowedType)
                    || allowedType.isAssignableFrom(finalCandidate));
            if (valid) return candidate;
            candidate = candidate.getSuperclass();
        }

        return Object.class;
    }

    @Override
    public Class<?>[] possibleReturnTypes() {
        Set<Class<?>> possibleReturnTypes = new HashSet<>();
        for (Literal<? extends ClassInfo<?>> literal : AddonUtils.getLiterals(classInfoExpression))
            for (ClassInfo<?> classInfo : literal.getAll())
                possibleReturnTypes.add(classInfo.getC());
        if (possibleReturnTypes.isEmpty())
            possibleReturnTypes.add(Object.class);
        return possibleReturnTypes.toArray(new Class[0]);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return (classInfoExpression == null ? "" : classInfoExpression.toString(event, debug) + " ")+"list containing";
    }

    @Override
    public List<AsyncableSyntax> getAsyncableSyntaxes() {
        return AsyncableSyntaxesWrapper.filterAsyncableSyntaxes(objectExpressionList.getExpressions());
    }
}
