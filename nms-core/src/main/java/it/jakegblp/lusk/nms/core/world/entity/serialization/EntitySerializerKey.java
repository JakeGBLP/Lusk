package it.jakegblp.lusk.nms.core.world.entity.serialization;

import org.jetbrains.annotations.NotNull;

public record EntitySerializerKey<T>(@NotNull Class<T> serializeableClass, @NotNull Type serializerType) {

    public EntitySerializerKey(@NotNull Class<T> serializerClass) {
        this(serializerClass, Type.NORMAL);
    }

    public static <T> EntitySerializerKey<T> normal(@NotNull Class<T> serializeableClass) {
        return new EntitySerializerKey<>(serializeableClass);
    }

    public static <T> EntitySerializerKey<T> optional(@NotNull Class<T> serializeableClass) {
        return new EntitySerializerKey<>(serializeableClass, Type.OPTIONAL);
    }

    public static <T> EntitySerializerKey<T> holder(@NotNull Class<T> serializeableClass) {
        return new EntitySerializerKey<>(serializeableClass, Type.HOLDER);
    }

    public static <T> EntitySerializerKey<T> list(@NotNull Class<T> serializeableClass) {
        return new EntitySerializerKey<>(serializeableClass, Type.LIST);
    }

    public enum Type {
        OPTIONAL, HOLDER, LIST, NORMAL
    }

}