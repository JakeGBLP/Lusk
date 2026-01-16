package it.jakegblp.lusk.nms.core.util;

/**
 * This interface does not specify which data is serialized, both full and partial serialization are allowed.
 * @param <T> the object to serialize
 */
public interface BufferSerializer<T> {
    void write(SimpleByteBuf buffer, T value);
    void read(SimpleByteBuf buffer, T value);
}
