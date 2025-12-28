package it.jakegblp.lusk.nms.core.util;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IdentityBufferCodec<Type> extends SimpleBufferCodec<Type, Type> {

    Class<Type> getType();

    default Class<Type> getFromClass() {
        return getType();
    }

    default Class<Type> getToClass() {
        return getType();
    }

    Type read(SimpleByteBuf buffer);

    void write(Type type, SimpleByteBuf buffer);

    @Override
    default void writeFrom(Type from, SimpleByteBuf buffer) throws IllegalArgumentException {
        writeTo(from, buffer);
    }

    @Override
    default Type readFrom(SimpleByteBuf buffer) throws IllegalArgumentException {
        return read(buffer);
    }

    @Override
    default void writeTo(Type to, SimpleByteBuf buffer) throws IllegalArgumentException {
        write(to, buffer);
    }

    @Override
    default Type readTo(SimpleByteBuf buffer) throws IllegalArgumentException {
        return read(buffer);
    }

    default Type encode(Type input) throws IllegalArgumentException {
        SimpleByteBuf buffer = new SimpleByteBuf();
        writeFrom(input, buffer);
        return readTo(buffer);
    }

    default Type decode(Type input) {
        SimpleByteBuf buffer = new SimpleByteBuf();
        writeTo(input, buffer);
        return readFrom(buffer);
    }
}
