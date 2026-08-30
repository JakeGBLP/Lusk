package it.jakegblp.lusk.nms.core.util;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.common.PseudoEnum;
import net.kyori.adventure.text.Component;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.*;
import java.util.function.Function;

public class NullabilityUtils {

    @SuppressWarnings("unchecked")
    @Contract("null -> null")
    public static <T> T copyIfNotNull(T value) {
        return (T) switch (value) {
            case Copyable<?> copyable -> copyable.copy();
            case Keyed ignored -> value;
            case ItemStack item -> item.clone();
            case Vector vector -> vector.clone();
            case BlockData blockData -> blockData.clone();
            case BoundingBox boundingBox -> boundingBox.clone();
            case Location location -> location.clone();
            case Component ignored -> value;
            case Quaternionf quaternionf -> {
                try {
                    yield quaternionf.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
            case Vector3i vector3i -> {
                try {
                    yield vector3i.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
            case Vector3f vector3f -> {
                try {
                    yield vector3f.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
            case Vector3d vector3d -> {
                try {
                    yield vector3d.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
            case String ignored -> value;
            case Number ignored -> value;
            case Boolean ignored -> value;
            case Enum<?> ignored -> value;
            case PseudoEnum<?> ignored -> value;
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
            case null -> null;
            default -> throw new IllegalStateException("Missing copying implementation for " + value.getClass().getSimpleName());
        };
    }

    public static <T, R> R convertIfNotNull(@Nullable T object, @NotNull Function<? super @NotNull T, ? extends R> converter) {
        if (object == null) return null;
        return converter.apply(object);
    }

}
