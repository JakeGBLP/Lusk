package it.jakegblp.lusk.nms.core.util;

import it.jakegblp.lusk.common.Copyable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class NullabilityUtils {

    @SuppressWarnings("unchecked")
    public static <T> T copyIfNotNull(T value) {
        if (value == null) return null;

        if (value instanceof ItemStack item) {
            return (T) item.clone();
        }

        if (value instanceof net.kyori.adventure.text.Component) {
            return value;
        }

        if (value instanceof List<?> list) {
            List<Object> copy = new ArrayList<>(list.size());
            for (Object o : list) {
                copy.add(copyIfNotNull(o));
            }
            return (T) copy;
        }

        if (value instanceof Set<?> set) {
            Set<Object> copy = new HashSet<>(set.size());
            for (Object o : set) {
                copy.add(copyIfNotNull(o));
            }
            return (T) copy;
        }

        if (value instanceof Map<?, ?> map) {
            Map<Object, Object> copy = new HashMap<>(map.size());
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                copy.put(copyIfNotNull(entry.getKey()), copyIfNotNull(entry.getValue()));
            }
            return (T) copy;
        }

        if (value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Enum<?>) {
            return value;
        }

        return value;
    }


    public static <T, R> R convertIfNotNull(@Nullable T object, @NotNull Function<? super @NotNull T, ? extends R> converter) {
        if (object == null) return null;
        return converter.apply(object);
    }

}
