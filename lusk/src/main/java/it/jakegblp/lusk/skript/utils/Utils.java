package it.jakegblp.lusk.skript.utils;

import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.parser.ParserInstance;
import it.jakegblp.lusk.skript.core.adapters.SkriptAdapter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Utils {
    public static SkriptAdapter skriptAdapter;

    public static <T> T getExpressionValue(@Nullable Expression<T> expression, Event event, T defaultValue) {
        return expression == null ? defaultValue : expression.getOptionalSingle(event).orElse(defaultValue);
    }

    public static Object getSectionContext(ParserInstance parserInstance) {
        return skriptAdapter.getSectionContext(parserInstance);
    }

    public static <T> T modifySectionContext(Object sectionContext, SectionNode sectionNode, List<TriggerItem> triggerItems, Supplier<? extends T> supplier) {
        return skriptAdapter.modifySectionContext(sectionContext, sectionNode, List.copyOf(triggerItems), supplier);
    }

    public static <T> T modifySectionContext(ParserInstance parserInstance, SectionNode sectionNode, List<TriggerItem> triggerItems, Supplier<? extends T> supplier) {
        return modifySectionContext(getSectionContext(parserInstance), sectionNode, List.copyOf(triggerItems), supplier);
    }

    @SuppressWarnings("unchecked")
    public static ExpressionList<?> parseSectionNodes(Section parentSection, SectionNode mainNode, Class<?> returnType, @Nullable BiConsumer<Node, String> failure, Class<?> @Nullable ... types) {
        ParserInstance parserInstance = ParserInstance.get();
        List<TriggerSection> previousSections = parserInstance.getCurrentSections();
        List<TriggerSection> sections = new ArrayList<>(previousSections);
        sections.add(parentSection);
        parserInstance.setCurrentSections(sections);
        Object sectionContext = getSectionContext(parserInstance);
        List<Expression<?>> expressionList = null;
        Class<?>[] possibleReturnTypes = types == null ? new Class[]{returnType} : types;
        for (Node subNode : mainNode) {
            String key = subNode.getKey();
            if (key != null) {
                Supplier<Expression<?>> supplier = () -> new SkriptParser(key).parseExpression(possibleReturnTypes);
                Expression<?> expression = modifySectionContext(sectionContext, subNode instanceof SectionNode sectionNode ? sectionNode : null, List.of(), supplier);
                if (expression != null) {
                    if (expressionList == null)
                        expressionList = new ArrayList<>();
                    expressionList.add(expression);
                } else if (failure != null) {
                    failure.accept(subNode, key);
                }
            }
        }
        parserInstance.setCurrentSections(previousSections);
        if (expressionList == null) return null;
        return new ExpressionList<>(expressionList.toArray(new Expression[0]), returnType, possibleReturnTypes, true);
    }

    public static ExpressionList<?> parseSectionNodes(Section parentSection, SectionNode mainNode, Class<?> returnType, Class<?> @Nullable ... types) {
        return parseSectionNodes(parentSection, mainNode, returnType, null, types);
    }

    public static <T> ExpressionList<T> parseSectionNodes(Section parentSection, SectionNode mainNode, Class<T> returnType) {
        return (ExpressionList<T>) parseSectionNodes(parentSection, mainNode, returnType, null, (Class<?>[]) null);
    }

    public static <T> List<? extends Literal<? extends T>> getLiterals(Expression<T> expression) {
        if (expression instanceof LiteralList<T> literalList)
            return Arrays.asList(literalList.getExpressions());
        else if (expression instanceof Literal<T> literal)
            return List.of(literal);
        else if (expression instanceof ExpressionList<T> expressionList)
            return Arrays.stream(expressionList.getExpressions()).filter(innerExpression -> innerExpression instanceof Literal).map(innerExpression -> (Literal<? extends T>) innerExpression).toList();
        return List.of();
    }


    public static <T> @Nullable T getSingleDefaultOrNull(Event event, @Nullable Expression<T> expression, T defaultValue) {
        return expression == null ? defaultValue : expression.getOptionalSingle(event).orElse(null);
    }

}
