package it.jakegblp.lusk.nms.core.util;

public interface IntermediarySimpleBufferCodec<From, Wrapped, To> extends SimpleBufferCodec<From, To> {

    default void writeWrapped(Wrapped from, SimpleByteBuf buffer) throws IllegalArgumentException {
        writeFrom(unwrap(from), buffer);
    }

    default Wrapped readWrapped(SimpleByteBuf buffer) throws IllegalArgumentException {
        return wrap(readFrom(buffer));
    }

    Wrapped wrap(From from);

    From unwrap(Wrapped wrapped);

}
