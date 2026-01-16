package it.jakegblp.lusk.nms.core.util;

import it.jakegblp.lusk.nms.core.world.entity.serialization.DataHolderType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface SimpleBufferCodec<From, To> extends BufferCodec {

    Class<From> getFromClass();

    Class<To> getToClass();

    void writeFrom(From from, SimpleByteBuf buffer) throws IllegalArgumentException;

    From readFrom(SimpleByteBuf buffer) throws IllegalArgumentException;

    void writeTo(To to, SimpleByteBuf buffer) throws IllegalArgumentException;

    To readTo(SimpleByteBuf buffer) throws IllegalArgumentException;

    DataHolderType getHolderType();

    default To encode(From input) throws IllegalArgumentException {
        SimpleByteBuf buffer = new SimpleByteBuf();
        writeFrom(input, buffer);
        return readTo(buffer);
    }

    default From decode(To input) {
        SimpleByteBuf buffer = new SimpleByteBuf();
        writeTo(input, buffer);
        return readFrom(buffer);
    }
}
