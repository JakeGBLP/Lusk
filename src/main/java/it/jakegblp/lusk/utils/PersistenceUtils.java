package it.jakegblp.lusk.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

public class PersistenceUtils {
    public static final PersistentDataType<?,?>[] DATA_TYPES = {
            PersistentDataType.BYTE,
            PersistentDataType.SHORT,
            PersistentDataType.INTEGER,
            PersistentDataType.LONG,
            PersistentDataType.FLOAT,
            PersistentDataType.DOUBLE,
            PersistentDataType.TAG_CONTAINER
    };

    @Nullable
    public static PersistentDataType<?,?> getPersistentDataType(PersistentDataContainer container, NamespacedKey key) {
        return Arrays.stream(DATA_TYPES).filter(persistentDataType -> container.has(key, persistentDataType)).findFirst().orElse(null);
    }

    @Nullable
    public static Object getValue(PersistentDataContainer container, NamespacedKey key) {
        PersistentDataType<?,?> dataType = getPersistentDataType(container, key);
        if (dataType == null) return null;
        return container.get(key, dataType);
    }

    public static String asString(PersistentDataContainer container) {
        if (container.isEmpty()) return "{}";
        StringBuilder builder = new StringBuilder("{");
        Set<NamespacedKey> keys = container.getKeys();
        int i = 0;
        for (NamespacedKey key : keys) {
            if (i > 0) builder.append(", ");
            PersistentDataType<?,?> dataType = getPersistentDataType(container, key);
            if (dataType == null) continue;
            Object value = container.get(key, dataType);
            builder.append(dataType).append(": ");
            if (value instanceof PersistentDataContainer innerContainer) {
                builder.append(asString(innerContainer));
            } else {
                builder.append(value);
            }
            i++;
        }
        return builder.append("}").toString();
    }
}
