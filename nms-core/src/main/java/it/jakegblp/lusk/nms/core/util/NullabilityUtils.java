package it.jakegblp.lusk.nms.core.util;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class NullabilityUtils {

    @SuppressWarnings("unchecked")
    public static <T> T cloneIfNotNull(T obj) {
        if (obj == null) return null;
        else if (obj instanceof FlagByte<?,?,?> flagByte) return (T) flagByte.clone();
        else if (obj instanceof BlockVector blockVector) return (T) blockVector.clone();
        throw new UnsupportedOperationException("Cannot clone " + obj.getClass().getName() + " comfortably!");
    }

    public static <T, R> R convertIfNotNull(@Nullable T object, @NotNull Function<? super @NotNull T, ? extends R> converter) {
        if (object == null) return null;
        return converter.apply(object);
    }

}
