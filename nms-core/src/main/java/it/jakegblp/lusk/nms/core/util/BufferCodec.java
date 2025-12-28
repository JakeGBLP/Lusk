package it.jakegblp.lusk.nms.core.util;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public interface BufferCodec {

    static <F, T> SimpleBufferCodec<F ,T> oneSided(
            Class<F> fromClass,
            Class<T> toClass,
            Function<F, T> toIntermediateFrom,
            Function<T, F> fromIntermediateFrom,
            Writer<F> writeIntermediate,
            Reader<F> readIntermediate
    ) {
        return BufferCodec.of(
                fromClass,
                toClass,
                writeIntermediate,
                readIntermediate,
                (buffer, to) -> writeIntermediate.write(buffer, fromIntermediateFrom.apply(to)),
                buffer -> toIntermediateFrom.apply(readIntermediate.read(buffer))
        );
    }

    static <T> IdentityBufferCodec<T> identity(
            Class<T> fromClass,
            Writer<T> writer,
            Reader<T> reader
    ) {
        return new IdentityBufferCodec<>() {
            @Override
            public Class<T> getType() {
                return fromClass;
            }

            @Override
            public T read(SimpleByteBuf buffer) {
                return reader.read(buffer);
            }

            @Override
            public void write(T t, SimpleByteBuf buffer) {
                writer.write(buffer, t);
            }
        };
    }

    static <F, T> SimpleBufferCodec<F, T> of(
            Class<F> fromClass,
            Class<T> toClass,
            Writer<F> writeFrom,
            Reader<F> readFrom,
            Writer<T> writeTo,
            Reader<T> readTo
    ) {
        return new SimpleBufferCodec<>() {
            @Override
            public Class<F> getFromClass() {
                return fromClass;
            }

            @Override
            public Class<T> getToClass() {
                return toClass;
            }

            @Override
            public void writeFrom(F from, SimpleByteBuf buffer) throws IllegalArgumentException {
                writeFrom.write(buffer, from);
            }

            @Override
            public F readFrom(SimpleByteBuf buffer) throws IllegalArgumentException {
                return readFrom.read(buffer);
            }

            @Override
            public void writeTo(T to, SimpleByteBuf buffer) throws IllegalArgumentException {
                writeTo.write(buffer, to);
            }

            @Override
            public T readTo(SimpleByteBuf buffer) throws IllegalArgumentException {
                return readTo.read(buffer);
            }
        };
    }

    @FunctionalInterface
    interface Reader<V> {
        V read(SimpleByteBuf buffer) throws IllegalArgumentException;
    }

    @FunctionalInterface
    interface Writer<V> {
        void write(SimpleByteBuf buffer, V value) throws IllegalArgumentException;
    }

}
