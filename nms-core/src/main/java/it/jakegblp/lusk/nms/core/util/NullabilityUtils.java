package it.jakegblp.lusk.nms.core.util;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.common.PseudoEnum;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class NullabilityUtils {

    @SuppressWarnings("unchecked")
    public static <T> T copyIfNotNull(T value) {
        return (T) switch (value) {
            case Copyable<?> copyable -> copyable.copy();
            case ItemStack item -> item.clone();
            case Component ignored -> value;
            case String ignored -> value;
            case Number ignored -> value;
            case Boolean ignored -> value;
            case Enum<?> ignored -> value;
            case PseudoEnum ignored -> value;
            case List<?> list -> {
                List<Object> copy = new ArrayList<>(list.size());
                for (Object o : list)
                    copy.add(copyIfNotNull(o));
                yield (T) copy;
            }
            case Set<?> set -> {
                Set<Object> copy = new HashSet<>(set.size());
                for (Object o : set)
                    copy.add(copyIfNotNull(o));
                yield (T) copy;
            }
            case Map<?, ?> map -> {
                Map<Object, Object> copy = new HashMap<>(map.size());
                for (Map.Entry<?, ?> entry : map.entrySet())
                    copy.put(copyIfNotNull(entry.getKey()), copyIfNotNull(entry.getValue()));
                yield (T) copy;
            }
            case null, default -> null;
        };
    }

    public static <T, R> R convertIfNotNull(@Nullable T object, @NotNull Function<? super @NotNull T, ? extends R> converter) {
        if (object == null) return null;
        return converter.apply(object);
    }

}
