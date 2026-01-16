package it.jakegblp.lusk.nms.core.util;

import it.jakegblp.lusk.common.Copyable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class NullabilityUtils {

    @SuppressWarnings("unchecked")
    public static <T> T copyIfNotNull(@Nullable T obj) {
        if (obj == null) return null;
        else if (obj instanceof Boolean || obj instanceof String || obj instanceof Number || obj instanceof Character) return obj;
        else if (obj instanceof Copyable<?> copyable) return (T) copyable.copy();
        throw new UnsupportedOperationException("Cannot copy " + obj.getClass().getName() + " comfortably!");
    }

    public static <T, R> R convertIfNotNull(@Nullable T object, @NotNull Function<? super @NotNull T, ? extends R> converter) {
        if (object == null) return null;
        return converter.apply(object);
    }

}
