package it.jakegblp.lusk.api;

import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.BiFunction;

public class PDCWrapper {

    public enum PersistentTagType {
        BYTE(PersistentDataType.BYTE, (container, key) -> container.get(key,PersistentDataType.BYTE), (container, key, o) -> container.set(key,PersistentDataType.BYTE, o)),
        SHORT(PersistentDataType.SHORT, (container, key) -> container.get(key,PersistentDataType.SHORT), (container, key, o) -> container.set(key,PersistentDataType.SHORT, o)),
        INTEGER(PersistentDataType.INTEGER, (container, key) -> container.get(key,PersistentDataType.INTEGER), (container, key, o) -> container.set(key,PersistentDataType.INTEGER, o)),
        LONG(PersistentDataType.LONG, (container, key) -> container.get(key,PersistentDataType.LONG), (container, key, o) -> container.set(key,PersistentDataType.LONG, o)),
        FLOAT(PersistentDataType.FLOAT, (container, key) -> container.get(key,PersistentDataType.FLOAT), (container, key, o) -> container.set(key,PersistentDataType.FLOAT, o)),
        DOUBLE(PersistentDataType.DOUBLE, (container, key) -> container.get(key,PersistentDataType.DOUBLE), (container, key, o) -> container.set(key,PersistentDataType.DOUBLE, o)),
        BOOLEAN(PersistentDataType.BOOLEAN, (container, key) -> container.get(key,PersistentDataType.BOOLEAN), (container, key, o) -> container.set(key,PersistentDataType.BOOLEAN, o)),
        STRING(PersistentDataType.STRING, (container, key) -> container.get(key,PersistentDataType.STRING), (container, key, o) -> container.set(key,PersistentDataType.STRING, o)),
        BYTE_ARRAY(PersistentDataType.BYTE_ARRAY, (container, key) -> container.get(key,PersistentDataType.BYTE_ARRAY), (container, key, o) -> container.set(key,PersistentDataType.BYTE_ARRAY, o)),
        INTEGER_ARRAY(PersistentDataType.INTEGER_ARRAY, (container, key) -> container.get(key,PersistentDataType.INTEGER_ARRAY), (container, key, o) -> container.set(key,PersistentDataType.INTEGER_ARRAY, o)),
        LONG_ARRAY(PersistentDataType.LONG_ARRAY, (container, key) -> container.get(key,PersistentDataType.LONG_ARRAY), (container, key, o) -> container.set(key,PersistentDataType.LONG_ARRAY, o)),
        TAG_CONTAINER(PersistentDataType.TAG_CONTAINER, (container, key) -> container.get(key,PersistentDataType.TAG_CONTAINER), (container, key, o) -> container.set(key,PersistentDataType.TAG_CONTAINER, o));

        private final PersistentDataType<?,?> type;
        private final BiFunction<PersistentDataContainer, NamespacedKey, Object> get;
        private final TriConsumer<PersistentDataContainer, NamespacedKey, Object> set;

        @SuppressWarnings("unchecked")
        <P,C> PersistentTagType(PersistentDataType<P,C> type, BiFunction<PersistentDataContainer, NamespacedKey, C> get, TriConsumer<PersistentDataContainer,NamespacedKey, C> set) {
            this.type = type;
            this.get = (BiFunction<PersistentDataContainer, NamespacedKey, Object>) get;
            this.set = (TriConsumer<PersistentDataContainer, NamespacedKey, Object>) set;
        }

        public PersistentDataType<?, ?> getType() {
            return type;
        }

        public Object get(PersistentDataContainer container, NamespacedKey key) {
            return get.apply(container, key);
        }

        public void set(PersistentDataContainer container, NamespacedKey key, Object o) {
            set.accept(container, key, o);
        }

        @Nullable
        public static PersistentTagType fromDataType(PersistentDataType<?, ?> type) {
            return Arrays.stream(values()).filter(persistentTagType -> persistentTagType.type == type).findFirst().orElse(null);
        }
    }

    PDCWrapper

}
