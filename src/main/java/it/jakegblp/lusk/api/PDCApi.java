package it.jakegblp.lusk.api;

import it.jakegblp.lusk.api.enums.PersistentTagType;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.function.Function;

public class PDCApi {

    @NullMarked
    public static void deleteTag(NamespacedKey key, PersistentDataContainer container) {
        container.remove(key);
    }

    public static void setTag(PersistentTagType tagType, NamespacedKey key, PersistentDataContainer container, Object[] objects) {
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
            case TAG_CONTAINER -> null;
        }
    }
}
