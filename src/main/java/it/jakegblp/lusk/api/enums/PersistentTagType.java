package it.jakegblp.lusk.api.enums;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public enum PersistentTagType {
    BYTE(PersistentDataType.BYTE),
    SHORT(PersistentDataType.SHORT),
    INTEGER(PersistentDataType.INTEGER),
    LONG(PersistentDataType.LONG),
    FLOAT(PersistentDataType.FLOAT),
    DOUBLE(PersistentDataType.DOUBLE),
    BOOLEAN(PersistentDataType.BOOLEAN),
    STRING(PersistentDataType.STRING),
    BYTE_ARRAY(PersistentDataType.BYTE_ARRAY, Byte[].class),
    INTEGER_ARRAY(PersistentDataType.INTEGER_ARRAY, Integer[].class),
    LONG_ARRAY(PersistentDataType.LONG_ARRAY, Long[].class),
    TAG_CONTAINER(PersistentDataType.TAG_CONTAINER);

    private final Class<?> complexClass;
    private final PersistentDataType<?,?> type;

    <P,C> PersistentTagType(PersistentDataType<P,C> type) {
        this.type = type;
        this.complexClass = type.getComplexType();
    }

    <P,C> PersistentTagType(PersistentDataType<P,C> type, Class<?> complexClass) {
        this.type = type;
        this.complexClass = complexClass;
    }

    public Class<?> getComplexClass() {
        return complexClass;
    }

    public PersistentDataType<?, ?> getDataType() {
        return type;
    }

    public static PersistentTagType getByTag(NamespacedKey key, PersistentDataContainer container) {
        for (PersistentTagType value : values()) {
            if (container.has(key, value.getDataType())) return value;
        }
        return null;
    }
}
