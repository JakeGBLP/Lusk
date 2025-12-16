package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static it.jakegblp.lusk.skript.utils.AddonUtils.errorForNode;
import static it.jakegblp.lusk.skript.utils.AddonUtils.parseSectionNodes;

public class ExprSecList extends SectionExpression<Object> implements AsyncableSyntaxesWrapper {

    static {
        Skript.registerExpression(ExprSecList.class, Object.class, ExpressionType.COMBINED, "[a[n]] [%-classinfos%] list [of|containing]");
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
        return LiteralUtils.canInitSafely(objectExpressionList);
    }

    @Override
    protected Object @Nullable [] get(Event event) {
        return Arrays.stream(objectExpressionList.getAll(event)).map(object -> {
            for (Class<?> possibleReturnType : possibleReturnTypes())
                if (possibleReturnType.isInstance(object))
                    return object;
            return null;
        }).filter(Objects::nonNull).toArray();
    }

    @Override
    public boolean isSingle() {
        return objectExpressionList.isSingle();
    }

    @Override
    public Class<?> getReturnType() {
        return objectExpressionList.getReturnType();
    }

    @Override
    public Class<?>[] possibleReturnTypes() {
        return CommonUtils.mapToArray(Class.class, CommonUtils.flatMap(AddonUtils.getLiterals(classInfoExpression), literal -> Arrays.asList(literal.getAll())), ClassInfo::getC);
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
