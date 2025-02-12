package it.jakegblp.lusk.api;

import it.jakegblp.lusk.api.enums.PersistentTagType;
import it.jakegblp.lusk.utils.LuskUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.*;

public class PDCApi {

    public record Nested(@NotNull PersistentDataContainer container, @NotNull NamespacedKey key) {}

    @NullMarked
    @Nullable
    public static Object getTag(PersistentDataContainer container, PersistentTagType tagType, String path) {
        Nested nested = getNestedContainer(container, path);
        if (nested == null) return null;
        return getTag(nested.container, tagType, nested.key);
    }

    @NullMarked
    @Nullable
    public static Object getTag(PersistentDataContainer container, PersistentTagType tagType, NamespacedKey key) {
        return container.get(key, tagType.getDataType());
    }

    @NullMarked
    public static void deleteTag(PersistentDataContainer container, String path) {
        setTag(container, null, path, null);

    }

    @NullMarked
    public static void deleteTag(PersistentDataContainer container, NamespacedKey key) {
        container.remove(key);
    }

    @NullMarked
    public static void setTag(PersistentDataContainer container, PersistentTagType tagType, NamespacedKey key, Object[] objects) {
        Object object = objects[0];
        switch (tagType) {
            case BOOLEAN -> {
                if (object instanceof Boolean bool) {
                    container.set(key, PersistentDataType.BOOLEAN, bool);
                }
            }
            case BYTE -> {
                if (object instanceof Number number) {
                    container.set(key, PersistentDataType.BYTE, number.byteValue());
                }
            }
            case SHORT -> {
                if (object instanceof Number number) {
                    container.set(key, PersistentDataType.SHORT, number.shortValue());
                }
            }
            case INTEGER -> {
                if (object instanceof Number number) {
                    container.set(key, PersistentDataType.INTEGER, number.intValue());
                }
            }
            case LONG -> {
                if (object instanceof Number number) {
                    container.set(key, PersistentDataType.LONG, number.longValue());
                }
            }
            case FLOAT -> {
                if (object instanceof Number number) {
                    container.set(key, PersistentDataType.FLOAT, number.floatValue());
                }
            }
            case DOUBLE -> {
                if (object instanceof Number number) {
                    container.set(key, PersistentDataType.DOUBLE, number.doubleValue());
                }
            }
            case STRING -> {
                if (object instanceof String s) {
                    container.set(key, PersistentDataType.STRING, s);
                }
            }
            case BYTE_ARRAY -> {
                if (objects instanceof Number[] numbers) {
                    byte[] bytes = new byte[numbers.length];
                    for (int i = 0; i < numbers.length; i++)
                        bytes[i] = numbers[i].byteValue();
                    container.set(key, PersistentDataType.BYTE_ARRAY, bytes);
                }
            }
            case INTEGER_ARRAY -> {
                if (objects instanceof Number[] numbers) {
                    container.set(key, PersistentDataType.INTEGER_ARRAY, Arrays.stream(numbers).mapToInt(Number::intValue).toArray());
                }
            }
            case LONG_ARRAY -> {
                if (objects instanceof Number[] numbers) {
                    container.set(key, PersistentDataType.LONG_ARRAY, Arrays.stream(numbers).mapToLong(Number::longValue).toArray());
                }
            }
            case TAG_CONTAINER -> {
                if (object instanceof PersistentDataContainer c) {
                    container.set(key, PersistentDataType.TAG_CONTAINER, c);
                }
            }
        }
    }

    @NullMarked
    public static void setTag(PersistentDataContainer container, @Nullable PersistentTagType tagType, String path, Object @Nullable [] objects) {
        List<String> parts = new ArrayList<>(List.of(path.split(";")));

        TreeMap<NamespacedKey, PersistentDataContainer> subContainers = new TreeMap<>();
        PersistentDataContainer currentContainer = container;

        for (int i = 0; i < parts.size(); i++) {
            String part = parts.get(i);
            NamespacedKey key = LuskUtils.getNamespacedKey(part);
            if (key == null) return;

            if (i < parts.size() - 1) {
                PersistentDataContainer nextContainer = currentContainer.getOrDefault(
                        key,
                        PersistentDataType.TAG_CONTAINER,
                        currentContainer.getAdapterContext().newPersistentDataContainer()
                );
                subContainers.putIfAbsent(key, nextContainer);
                currentContainer = nextContainer;
            } else if (objects == null || tagType == null) {
                deleteTag(currentContainer, key);
            } else {
                setTag(currentContainer, tagType, key, objects);
            }
        }

        subContainers.descendingMap().forEach((key, nested) -> {
            PersistentDataContainer parent = subContainers.lowerEntry(key) == null ? container
                    : subContainers.lowerEntry(key).getValue();

            parent.set(key, PersistentDataType.TAG_CONTAINER, nested);
        });
    }

    @Nullable
    @NullMarked
    public static Nested getNestedContainer(PersistentDataContainer container, String path) {
        NamespacedKey key;
        if (path.contains(";")) {
            List<String> parts = new ArrayList<>(List.of(path.split(";")));
            String lastPart = parts.removeLast();
            PersistentDataContainer subContainer = container;
            for (String part : parts) {
                NamespacedKey subKey = LuskUtils.getNamespacedKey(part);
                if (subKey == null) return null;
                if (subContainer.has(subKey, PersistentDataType.TAG_CONTAINER)) {
                    subContainer = container.get(subKey, PersistentDataType.TAG_CONTAINER);
                    assert subContainer != null;
                } else return null;
            }
            key = LuskUtils.getNamespacedKey(lastPart);
        } else key = LuskUtils.getNamespacedKey(path);
        if (key == null) return null;
        return new Nested(container, key);
    }

    @NullMarked
    public static boolean hasTag(String tag, PersistentDataContainer container) {
        return getNestedContainer(container, tag) != null;
    }

}
