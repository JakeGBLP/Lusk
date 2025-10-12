package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.jakegblp.lusk.skript.utils.Utils.parseSectionNodes;

public class ExprSecList extends SectionExpression<Object> {

    static {
        Skript.registerExpression(ExprSecList.class, Object.class, ExpressionType.SIMPLE, "[a[n]] [%-classinfos%] list [of|containing]");
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
        for (Literal<? extends ClassInfo<?>> literal : Utils.getLiterals(classInfoExpression)) {
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
        AtomicBoolean error = new AtomicBoolean(false);
        objectExpressionList = parseSectionNodes(this.getAsSection(), node, Object.class, (subNode, key) -> {
            Skript.error(key + " is " + SkriptParser.notOfType(allowedClassesArray));
            error.set(true);
        }, allowedClasses.toArray(Class[]::new));
        if (objectExpressionList == null) {
            Skript.error("Parsed empty list section expression.");
            return false;
        } else if (error.get())
            return false;
        objectExpressionList = (ExpressionList<?>) LiteralUtils.defendExpression(objectExpressionList);
        return LiteralUtils.canInitSafely(objectExpressionList);
    }

    @Override
    protected Object @Nullable [] get(Event event) {
        return objectExpressionList.getAll(event);
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
        return (classInfoExpression instanceof Literal<ClassInfo<?>> classInfoLiteral ? Arrays.stream(classInfoLiteral.getAll()).map(ClassInfo::getC).toArray(Class[]::new) : super.possibleReturnTypes());
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return (classInfoExpression == null ? "" : classInfoExpression.toString(event, debug) + " ")+"list containing";
    }

}
