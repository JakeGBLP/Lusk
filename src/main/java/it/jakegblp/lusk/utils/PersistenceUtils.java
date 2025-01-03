package it.jakegblp.lusk.utils;

import it.jakegblp.lusk.api.enums.PersistentTagType;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PersistenceUtils {
    @Nullable
    public static PersistentTagType getPersistentDataType(PersistentDataContainer container, NamespacedKey key) {
        for (PersistentTagType value : PersistentTagType.values()) {
            if (container.has(key, value.getType()))
                return value;
        }
        return null;
    }

    @Nullable
    public static Object getValue(PersistentDataContainer container, NamespacedKey key) {
        PersistentTagType tag = getPersistentDataType(container, key);
        if (tag == null) return null;
        return container.get(key, tag.getType());
    }

    public static String asString(PersistentDataContainer container) {
        if (container.isEmpty()) return "{}";
        StringBuilder builder = new StringBuilder("{");
        Set<NamespacedKey> keys = container.getKeys();
        int i = 0;
        for (NamespacedKey key : keys) {
            if (i > 0) builder.append(", ");
            PersistentTagType tagType = getPersistentDataType(container, key);
            if (tagType == null) continue;
            Object value = tagType.get(container,key);
            builder.append('"').append(key.asString()).append("\": ");
            switch (tagType) {
                case BYTE -> builder.append(value).append('b');
                case SHORT -> builder.append(value).append('s');
                case INTEGER -> builder.append(value);
                case LONG -> builder.append(value).append('l');
                case FLOAT -> builder.append(value).append('f') ;
                case DOUBLE -> builder.append(value);
                case BOOLEAN -> builder.append(value);
                case STRING -> builder.append('"').append(value).append('"');
                case BYTE_ARRAY -> builder.append("-").append(value).append("-");
                case INTEGER_ARRAY -> builder.append("-").append(value).append("-");
                case LONG_ARRAY -> builder.append("-").append(value).append("-");
                case TAG_CONTAINER -> builder.append(asString((PersistentDataContainer) value));
            }
            i++;
        }
        return builder.append("}").toString();
    }
}
