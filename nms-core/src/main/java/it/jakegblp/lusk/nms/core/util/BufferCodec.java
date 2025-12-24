package it.jakegblp.lusk.nms.core.util;

import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface BufferCodec<From, To> extends Codec<From, To, IllegalArgumentException, IllegalArgumentException> {

    static <From, To> Builder<From, To> builder(Class<From> fromClass, Class<To> toClass) {
        return new Builder<>(fromClass, toClass);
    }

    class Builder<From, To> {
        private BiConsumer<From, SimpleByteBuf> writeFrom;
        private BiConsumer<To, SimpleByteBuf> writeTo;
        private Function<SimpleByteBuf, From> readFrom;
        private Function<SimpleByteBuf, To> readTo;
        private final Class<From> fromClass;
        private final Class<To> toClass;

        public Builder(Class<From> fromClass, Class<To> toClass) {
            this.fromClass = fromClass;
            this.toClass = toClass;
        }

        public Builder<From, To> writeFrom(BiConsumer<From, SimpleByteBuf> write) {
            this.writeFrom = write;
            return this;
        }
        public Builder<From, To> writeTo(BiConsumer<To, SimpleByteBuf> write) {
            this.writeTo = write;
            return this;
        }
        public Builder<From, To> readFrom(Function<SimpleByteBuf, From> read) {
            this.readFrom = read;
            return this;
        }
        public Builder<From, To> readTo(Function<SimpleByteBuf, To> read) {
            this.readTo = read;
            return this;
        }
        public BufferCodec<From, To> build() {
            return new BufferCodec<>() {
                @Override
                public void writeFrom(@NonNull From from, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                    writeFrom.accept(from, buffer);
                }

                @Override
                public From readFrom(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                    return readFrom.apply(buffer);
                }

                @Override
                public void writeTo(@NonNull To p, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                    writeTo.accept(p, buffer);
                }

                @Override
                public To readTo(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                    return readTo.apply(buffer);
                }
            };
        }
        public TypedBufferCodec<From, To> buildTyped() {
            return new TypedBufferCodec<>() {
                @Override
                public Class<From> getFromClass() {
                    return fromClass;
                }

                @Override
                public Class<To> getToClass() {
                    return toClass;
                }

                @Override
                public void writeFrom(@NonNull From from, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                    writeFrom.accept(from, buffer);
                }

                @Override
                public From readFrom(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                    return readFrom.apply(buffer);
                }

                @Override
                public void writeTo(@NonNull To p, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                    writeTo.accept(p, buffer);
                }

                @Override
                public To readTo(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                    return readTo.apply(buffer);
                }
            };
        }
    }


    void writeFrom(@NotNull From from, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException;

    From readFrom(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException;

    void writeTo(@NotNull To to, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException;

    To readTo(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException;

    @Override
    default @NotNull To encode(@NotNull From input) throws IllegalArgumentException {
        SimpleByteBuf buffer = new SimpleByteBuf();
        writeFrom(input, buffer);
        return readTo(buffer);
    }

    @Override
    default @NotNull From decode(@NotNull To input) {
        SimpleByteBuf buffer = new SimpleByteBuf();
        writeTo(input, buffer);
        return readFrom(buffer);
    }

    static <F, T> TypedBufferCodec<F ,T> simple(
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


    static <T> BufferCodec<T, T> identity(
            BufferCodec.Writer<T> writer,
            BufferCodec.Reader<T> reader
    ) {
        return BufferCodec.of(writer, reader, writer, reader);
    }

    static <T> TypedBufferCodec<T, T> identity(
            Class<T> fromClass,
            BufferCodec.Writer<T> writer,
            BufferCodec.Reader<T> reader
    ) {
        return BufferCodec.of(fromClass, fromClass, writer, reader, writer, reader);
    }

    static <F, T> TypedBufferCodec<F, T> of(
            Class<F> fromClass,
            Class<T> toClass,
            Writer<F> writeFrom,
            Reader<F> readFrom,
            Writer<T> writeTo,
            Reader<T> readTo
    ) {
        return new TypedBufferCodec<>() {
            @Override
            public Class<F> getFromClass() {
                return fromClass;
            }

            @Override
            public Class<T> getToClass() {
                return toClass;
            }

            @Override
            public void writeFrom(@NotNull F from, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                writeFrom.write(buffer, from);
            }

            @Override
            public F readFrom(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                return readFrom.read(buffer);
            }

            @Override
            public void writeTo(@NotNull T to, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                writeTo.write(buffer, to);
            }

            @Override
            public T readTo(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                return readTo.read(buffer);
            }
        };
    }

    static <F, T> BufferCodec<F, T> of(
            Writer<F> writeFrom,
            Reader<F> readFrom,
            Writer<T> writeTo,
            Reader<T> readTo
    ) {
        return new BufferCodec<>() {
            @Override
            public void writeFrom(@NotNull F from, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                writeFrom.write(buffer, from);
            }

            @Override
            public F readFrom(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                return readFrom.read(buffer);
            }

            @Override
            public void writeTo(@NotNull T to, @NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                writeTo.write(buffer, to);
            }

            @Override
            public T readTo(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException {
                return readTo.read(buffer);
            }
        };
    }

    @FunctionalInterface
    interface Reader<V> {
        V read(@NotNull SimpleByteBuf buffer) throws IllegalArgumentException;
    }

    @FunctionalInterface
    interface Writer<V> {
        void write(@NotNull SimpleByteBuf buffer, V value) throws IllegalArgumentException;
    }

}
