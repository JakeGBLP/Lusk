package it.jakegblp.lusk.nms.core.serialization;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public sealed interface TypeKey<T> permits SimpleKey, OptionalKey, ListKey, HolderKey {

    int variant();

    Class<T> type();

    Class<?> rawType();

    default boolean isOptional() {
        return this instanceof OptionalKey;
    }
    default boolean isSimple() {
        return this instanceof SimpleKey;
    }
    default boolean isList() {
        return this instanceof ListKey;
    }

    @Contract("_ -> new")
    static <T> @NotNull TypeKey<T> simple(Class<T> type) {
        return new SimpleKey<>(type, 0);
    }
    @Contract("_ -> new")
    static <T> @NotNull OptionalKey<T> optional(Class<T> type) {
        return new OptionalKey<>(simple(type), 0);
    }
    @Contract("_ -> new")
    static <T> @NotNull ListKey<T> list(Class<T> type) {
        return new ListKey<>(simple(type), 0);
    }
    @Contract("_ -> new")
    static <T> @NotNull TypeKey<T> holder(Class<T> type) {
        return new HolderKey<>(simple(type), 0);
    }
    @Contract("_ -> new")
    static <T> @NotNull OptionalKey<T> optional(@NotNull TypeKey<T> type) {
        return optional(type.type());
    }
    @Contract("_ -> new")
    static <T> @NotNull ListKey<T> list(@NotNull TypeKey<T> type) {
        return list(type.type());
    }
    @Contract("_ -> new")
    static <T> @NotNull TypeKey<T> holder(@NotNull TypeKey<T> type) {
        return new HolderKey<>(type, 0);
    }

    static <T, K extends TypeKey<T>> @NotNull K withVariant(@NotNull K key, int variant) {
        return (K) switch (key) {
            case SimpleKey<?> simple -> new SimpleKey<>(simple.type(), variant);
            case OptionalKey<?> optional -> new OptionalKey<>(optional.inner(), variant);
            case ListKey<?> list -> new ListKey<>(list.inner(), variant);
            case HolderKey<?> holder -> new HolderKey<>(holder.inner(), variant);
        };
    }



}