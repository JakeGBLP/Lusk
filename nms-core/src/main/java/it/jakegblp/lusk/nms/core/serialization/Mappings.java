package it.jakegblp.lusk.nms.core.serialization;

import it.jakegblp.lusk.common.CommonUtils;
import org.bukkit.Keyed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Mappings {

    @Contract(pure = true)
    @UnmodifiableView
    @NotNull List<Mapper<?, ?>> getMappers();

    <F, T> Mapper<F, T> register(
            Class<F> fromClass,
            Class<T> toClass,
            Function<@NotNull F, T> to,
            Function<@NotNull T, F> from
    );

    default List<Class<?>> getFromClasses() {
        return CommonUtils.map(getMappers(), Mapper::fromClass);
    }

    default List<Class<?>> getToClasses() {
        return CommonUtils.map(getMappers(), Mapper::toClass);
    }

    default boolean canConvertForward(Class<?> clazz) {
        for (Mapper<?, ?> mapper : getMappers())
            if (mapper.fromClass().isAssignableFrom(clazz))
                return true;
        return false;
    }

    default boolean mapsToForwardClass(Object object, Class<?> clazz) {
        for (Mapper<?, ?> mapper : getMappers())
            if (mapper.toClass().isAssignableFrom(clazz))
                return mapper.fromClass().isInstance(object);
        return false;
    }

    default boolean canConvertBackward(Class<?> clazz) {
        for (Mapper<?, ?> mapper : getMappers())
            if (mapper.toClass().isAssignableFrom(clazz))
                return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    default <T> Mapper<?, T> getMapperFromForwardClass(Class<T> forwardClass) {
        for (Mapper<?, ?> mapper : getMappers())
            if (mapper.toClass().isAssignableFrom(forwardClass))
                return (Mapper<?, T>) mapper;
        throw new IllegalArgumentException("Could not find mapper for forward class " + forwardClass.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    default <F> Mapper<F, ?> getMapperFromBackwardClass(Class<F> backwardClass) {
        for (Mapper<?, ?> mapper : getMappers())
            if (mapper.fromClass().isAssignableFrom(backwardClass))
                return (Mapper<F, ?>) mapper;
        throw new IllegalArgumentException("Could not find mapper for backward class " + backwardClass);
    }

    <F, T> BufferCodecMapper<F, T> register(
            Class<F> fClass,
            BiConsumer<SimpleByteBuf, F> writer, Function<SimpleByteBuf, F> reader,
            BufferCodec<T> toBufferCodec
    );

    <M extends Mapper<F, T>, F, T> M register(@NotNull M mapper);

    @Contract(pure = true)
    <F extends Enum<F>, T extends Enum<T>> EnumMapper<F, T> register(Class<F> from, Class<T> to);

    @Contract(pure = true)
    <F, T extends Keyed> RegistryMapper<F, T> registerRegistryUnsafe(Class<F> from, Class<T> to, Object nmsRegistry);

    <F, T> BufferCodecMapper<F, T> registerStreamCodecUnsafe(
            Class<F> fClass,
            Object streamCodec,
            BufferCodec<T> toBufferCodec);

    <F, T> T map(F f);

    <T, F> F mapBack(T t);
}
