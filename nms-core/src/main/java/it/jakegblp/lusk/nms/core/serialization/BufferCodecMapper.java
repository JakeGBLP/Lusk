package it.jakegblp.lusk.nms.core.serialization;

import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public interface BufferCodecMapper<F, T> extends Mapper<F, T> {

    static <F, T> BufferCodecMapper<F, T> of(
            BufferCodec<F> fromStreamCodec,
            BufferCodec<T> toStreamCodec
    ) {
        return of(fromStreamCodec, toStreamCodec, 0);
    }

    static <F, T> BufferCodecMapper<F, T> of(
            Class<F> fClass,
            BiConsumer<SimpleByteBuf, F> writer, Function<SimpleByteBuf, F> reader,
            BufferCodec<T> toStreamCodec
    ) {
        return of(BufferCodec.of(fClass, writer, reader), toStreamCodec, 0);
    }

    static <F, T> BufferCodecMapper<F, T> of(
            BufferCodec<F> fromStreamCodec,
            BufferCodec<T> toStreamCodec,
            int variant
    ) {
        return new BufferCodecMapper<>() {
            @Override
            public Class<F> fromClass() {
                return fromStreamCodec.type();
            }

            @Override
            public Class<T> toClass() {
                return toStreamCodec.type();
            }

            @Override
            public T to(F fromValue) {
                var buffer = new SimpleByteBuf();
                fromStreamCodec.write(buffer, fromValue);
                return toStreamCodec.read(buffer);
            }

            @Override
            public F from(T toValue) {
                var buffer = new SimpleByteBuf();
                toStreamCodec.write(buffer, toValue);
                return fromStreamCodec.read(buffer);
            }

            @Override
            public int variant() {
                return variant;
            }
        };
    }

    @Override
    default BufferCodecMapper<F, T> withVariant(int variant) {
        return new BufferCodecMapper<>() {

            @Override
            public Class<F> fromClass() {
                return BufferCodecMapper.this.fromClass();
            }

            @Override
            public Class<T> toClass() {
                return BufferCodecMapper.this.toClass();
            }

            @Override
            public T to(F from) {
                return BufferCodecMapper.this.to(from);
            }

            @Override
            public F from(T to) {
                return BufferCodecMapper.this.from(to);
            }

            @Override
            public int variant() {
                return variant;
            }
        };
    }
}
