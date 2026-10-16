package it.jakegblp.lusk.skript.api.registration;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.lang.util.SimpleExpression;
import it.jakegblp.lusk.common.Instances;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.DefaultSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Registrations {

    @NullMarked
    @Contract("_, _, _ -> new")
    public static <E extends SimpleExpression<T>, T, B extends SimpleExpressionBuilder<E, T, B>> SimpleExpressionBuilder<E, T, B> expression(Class<E> expressionClass, Supplier<E> supplier, Class<T> returnType) {
        return new SimpleExpressionBuilder<>(expressionClass, supplier, returnType);
    }

    @NullMarked
    @Contract("_, _, _ -> new")
    public static <E extends Expression<T>, T, B extends PropertyExpressionBuilder<E, T, B>> PropertyExpressionBuilder<E, T, B> propertyExpression(Class<E> expressionClass, Supplier<E> supplier, Class<T> returnType) {
        return new PropertyExpressionBuilder<>(expressionClass, supplier, returnType);
    }

    @NullMarked
    @Contract("_, _ -> new")
    public static <S extends Section, B extends SectionBuilder<S, B>> SectionBuilder<S, B> section(Class<S> sectionClass, Supplier<S> supplier) {
        return new SectionBuilder<>(sectionClass, supplier);
    }

    @NullMarked
    @Contract("_, _ -> new")
    public static <C extends Condition, B extends SimpleConditionBuilder<C, B>> SimpleConditionBuilder<C, B> condition(Class<C> conditionClass, Supplier<C> supplier) {
        return new SimpleConditionBuilder<>(conditionClass, supplier);
    }

    @NullMarked
    @Contract("_, _ -> new")
    public static <C extends PropertyCondition<?>, B extends PropertyConditionBuilder<C, B>> PropertyConditionBuilder<C, B> propertyCondition(Class<C> expressionClass, Supplier<C> supplier) {
        return new PropertyConditionBuilder<>(expressionClass, supplier);
    }

    @NullMarked
    @Contract("_, _ -> new")
    public static <E extends Effect, B extends EffectBuilder<E, B>> EffectBuilder<E, B> effect(Class<E> effectClass, Supplier<E> supplier) {
        return new EffectBuilder<>(effectClass, supplier);
    }

    @SafeVarargs
    @NullMarked
    @Contract("_, _ -> new")
    public static <E extends SkriptEvent, B extends EventBuilder<E, B>> EventBuilder<E, B> event(Class<E> eventClass, Class<? extends Event>... events) {
        return new EventBuilder<>(eventClass, events);
    }

    @SafeVarargs
    @NullMarked
    @Contract("_ -> new")
    public static <B extends EventBuilder<SimpleEvent, B>> EventBuilder<SimpleEvent, B> event(Class<? extends Event>... events) {
        return new EventBuilder<>(SimpleEvent.class, events);
    }

    @NullMarked
    public static <E extends Event, T> void registerEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, @Nullable T> getter
    ) {
        Instances.SKRIPT_ADDON.registry(EventValueRegistry.class).register(EventValue.simple(eventClass, valueClass, getter::apply));
    }

    @NullMarked
    public static <E extends Event, T> void registerFutureEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, @Nullable T> getter
    ) {
        Instances.SKRIPT_ADDON.registry(EventValueRegistry.class).register(EventValue.builder(eventClass, valueClass)
                .getter(getter::apply)
                .time(EventValue.Time.FUTURE)
                .build());
    }

    @NullMarked
    public static <E extends Event, T> void registerEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, T> getter,
            BiConsumer<E, T> setter
    ) {
        Instances.SKRIPT_ADDON.registry(EventValueRegistry.class).register(
                EventValue.builder(eventClass, valueClass)
                        .getter(getter::apply)
                        .registerChanger(Changer.ChangeMode.SET, setter::accept)
                        .build());
    }

    @NullMarked
    public static <E extends Event, T> void registerEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            BiFunction<T, T, T> add,
            BiFunction<T, T, T> remove
    ) {
        Instances.SKRIPT_ADDON.registry(EventValueRegistry.class).register(
                EventValue.builder(eventClass, valueClass)
                        .getter(getter::apply)
                        .registerChanger(Changer.ChangeMode.SET, setter::accept)
                        .registerChanger(Changer.ChangeMode.ADD, (event, value) -> setter.accept(event, add.apply(getter.apply(event), value)))
                        .registerChanger(Changer.ChangeMode.REMOVE, (event, value) -> setter.accept(event, remove.apply(getter.apply(event), value)))
                        .build());
    }

    protected interface HasPatterns<This extends HasPatterns<This>> {
        This patterns(String... patterns);
    }

    protected interface HasDocumentation<This extends HasDocumentation<This>> {
        This name(String name);

        This description(String description);

        This examples(String... examples);

        This since(String... since);

        This keywords(String... keywords);
    }

    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static abstract class Builder<S extends SyntaxElement, B extends Builder<S, B>> {
        protected final Class<S> elementClass;
        protected final Supplier<S> elementSupplier;

        public abstract void register(SyntaxRegistry syntaxRegistry);
    }

    protected static abstract class ExpressionBuilder<E extends Expression<T>, T, B extends ExpressionBuilder<E, T, B>>
            extends Builder<E, B> {

        protected final Class<T> returnType;

        protected ExpressionBuilder(Class<E> elementClass, Supplier<E> elementSupplier, Class<T> returnType) {
            super(elementClass, elementSupplier);
            this.returnType = returnType;
        }
    }

    public static class SimpleExpressionBuilder<E extends SimpleExpression<T>, T, B extends SimpleExpressionBuilder<E, T, B>>
            extends ExpressionBuilder<E, T, B>
            implements HasPatterns<B> {

        private String[] patterns;

        protected SimpleExpressionBuilder(Class<E> elementClass, Supplier<E> elementSupplier, Class<T> returnType) {
            super(elementClass, elementSupplier, returnType);
        }

        @Override
        public void register(SyntaxRegistry syntaxRegistry) {
            syntaxRegistry.register(SyntaxRegistry.EXPRESSION,
                    DefaultSyntaxInfos.Expression.builder(elementClass, returnType)
                            .addPatterns(patterns)
                            .supplier(elementSupplier)
                            .build()
            );
        }

        @SuppressWarnings("unchecked")
        @Override
        public B patterns(String... patterns) {
            this.patterns = patterns;
            return (B) this;
        }
    }

    public static class PropertyExpressionBuilder<E extends Expression<T>, T, B extends PropertyExpressionBuilder<E, T, B>>
            extends ExpressionBuilder<E, T, B> {

        private String property, type;
        private boolean defaultExpression;

        protected PropertyExpressionBuilder(Class<E> elementClass, Supplier<E> elementSupplier, Class<T> returnType) {
            super(elementClass, elementSupplier, returnType);
        }

        @SuppressWarnings("unchecked")
        public B propertyOf(String property, String type) {
            this.property = property;
            this.type = type;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B defaultExpression(boolean defaultExpression) {
            this.defaultExpression = defaultExpression;
            return (B) this;
        }

        @Override
        public void register(SyntaxRegistry syntaxRegistry) {
            syntaxRegistry.register(SyntaxRegistry.EXPRESSION,
                    PropertyExpression.infoBuilder(elementClass, returnType, property, type, defaultExpression)
                            .supplier(elementSupplier)
                            .build()
            );
        }
    }

    public static class SectionBuilder<S extends Section, B extends SectionBuilder<S, B>>
            extends Builder<S, B>
            implements HasPatterns<B> {

        private String[] patterns;

        protected SectionBuilder(Class<S> elementClass, Supplier<S> elementSupplier) {
            super(elementClass, elementSupplier);
        }

        @SuppressWarnings("unchecked")
        @Override
        public B patterns(String... patterns) {
            this.patterns = patterns;
            return (B) this;
        }

        @Override
        public void register(SyntaxRegistry syntaxRegistry) {
            syntaxRegistry.register(SyntaxRegistry.SECTION, SyntaxInfo.builder(elementClass)
                    .addPatterns(patterns)
                    .supplier(elementSupplier)
                    .build()
            );
        }
    }

    protected static abstract class ConditionBuilder<C extends Condition, B extends ConditionBuilder<C, B>>
            extends Builder<C, B> {

        protected ConditionBuilder(Class<C> elementClass, Supplier<C> elementSupplier) {
            super(elementClass, elementSupplier);
        }
    }

    public static class SimpleConditionBuilder<C extends Condition, B extends SimpleConditionBuilder<C, B>>
            extends ConditionBuilder<C, B>
            implements HasPatterns<B> {

        private String[] patterns;

        protected SimpleConditionBuilder(Class<C> elementClass, Supplier<C> elementSupplier) {
            super(elementClass, elementSupplier);
        }

        @SuppressWarnings("unchecked")
        @Override
        public B patterns(String... patterns) {
            this.patterns = patterns;
            return (B) this;
        }

        @Override
        public void register(SyntaxRegistry syntaxRegistry) {
            syntaxRegistry.register(SyntaxRegistry.CONDITION, SyntaxInfo.builder(elementClass)
                    .addPatterns(patterns)
                    .supplier(elementSupplier)
                    .build()
            );
        }
    }

    public static class PropertyConditionBuilder<C extends Condition, B extends PropertyConditionBuilder<C, B>>
            extends ConditionBuilder<C, B> {

        private PropertyCondition.PropertyType propertyType;
        private String property, type;

        protected PropertyConditionBuilder(Class<C> elementClass, Supplier<C> elementSupplier) {
            super(elementClass, elementSupplier);
        }

        @SuppressWarnings("unchecked")
        public B property(String type, PropertyCondition.PropertyType propertyType, String property) {
            this.type = type;
            this.propertyType = propertyType;
            this.property = property;
            return (B) this;
        }

        @Override
        public void register(SyntaxRegistry syntaxRegistry) {
            syntaxRegistry.register(SyntaxRegistry.CONDITION,
                    PropertyCondition.infoBuilder(elementClass, propertyType, property, type)
                            .supplier(elementSupplier)
                            .build()
            );
        }
    }

    public static class EffectBuilder<E extends Effect, B extends EffectBuilder<E, B>>
            extends Builder<E, B>
            implements HasPatterns<B> {

        private String[] patterns;

        protected EffectBuilder(Class<E> elementClass, Supplier<E> elementSupplier) {
            super(elementClass, elementSupplier);
        }

        @SuppressWarnings("unchecked")
        @Override
        public B patterns(String... patterns) {
            this.patterns = patterns;
            return (B) this;
        }

        @Override
        public void register(SyntaxRegistry syntaxRegistry) {
            syntaxRegistry.register(SyntaxRegistry.EFFECT, SyntaxInfo.builder(elementClass)
                    .addPatterns(patterns)
                    .supplier(elementSupplier)
                    .build()
            );
        }
    }

    public static class EventBuilder<E extends SkriptEvent, B extends EventBuilder<E, B>>
            extends Builder<E, B>
            implements HasPatterns<B>, HasDocumentation<B> {

        private final Class<? extends Event>[] events;
        private String[] patterns, examples, since, keywords;
        private String name, description;

        @SafeVarargs
        protected EventBuilder(Class<E> elementClass, Class<? extends Event>... events) {
            super(elementClass, null);
            this.events = events;
        }

        @SuppressWarnings("unchecked")
        @Override
        public B patterns(String... patterns) {
            this.patterns = patterns;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public B name(String name) {
            this.name = name;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public B description(String description) {
            this.description = description;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public B examples(String... examples) {
            this.examples = examples;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public B since(String... since) {
            this.since = since;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public B keywords(String... keywords) {
            this.keywords = keywords;
            return (B) this;
        }

        @Override
        public void register(SyntaxRegistry syntaxRegistry) {
            syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(elementClass, name)
                    .addEvents(events)
                    .addDescription(description)
                    .addExamples(examples)
                    .addKeywords(keywords)
                    .addSince(since)
                    .addPatterns(patterns)
                    .build()
            );
        }
    }
}
