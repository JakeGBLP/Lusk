package it.jakegblp.lusk.nms.core.util;

import lombok.Getter;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NullMarked
public class CompositeBufferCodec<From, To> implements TypedBufferCodec<From, To> {

    public static <From, To> Builder<From, To> builder(Class<From> fromClass, Class<To> toClass) {
        return new Builder<>(fromClass, toClass);
    }

    @Getter
    private final Class<From> fromClass;
    @Getter
    private final Class<To> toClass;
    private final List<Entry<From, To, ?, ?>> entries;
    private final Function<SimpleByteBuf, From> from;
    private final Function<SimpleByteBuf, To> to;

    protected CompositeBufferCodec(
            Class<From> fromClass, Class<To> toClass, List<Entry<From, To, ?, ?>> entries,
            Function<SimpleByteBuf, From> from, Function<SimpleByteBuf, To> to
    ) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.entries = List.copyOf(entries);
        this.from = from;
        this.to = to;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeFrom(From from, SimpleByteBuf buffer) {
        for (Entry<From, To, ?, ?> entry : entries) {
            Object value = entry.getterFrom.apply(from);
            ((BufferCodec<Object, Object>) entry.codec).writeFrom(value, buffer);
        }
    }

    @Override
    public From readFrom(SimpleByteBuf buffer) {
        return from.apply(buffer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeTo(To to, SimpleByteBuf buffer) {
        for (Entry<From, To, ?, ?> entry : entries) {
            Object value = entry.getterTo.apply(to);
            ((BufferCodec<Object, Object>) entry.codec).writeTo(value, buffer);
        }
    }

    @Override
    public To readTo(SimpleByteBuf buffer) {
        return to.apply(buffer);
    }

    public record Entry<From, To, FromValue, ToValue>(
            BufferCodec<FromValue, ToValue> codec,
            Function<From, FromValue> getterFrom,
            Function<To, ToValue> getterTo
    ) {}

    public static class Builder<From, To> {
        private final Class<From> fromClass;
        private final Class<To> toClass;
        private final List<Entry<From, To, ?, ?>> entries = new ArrayList<>();

        protected Builder(Class<From> fromClass, Class<To> toClass) {
            this.fromClass = fromClass;
            this.toClass = toClass;
        }

        public <FromValue, ToValue> Builder<From, To> with(
                BufferCodec<FromValue, ToValue> codec,
                Function<From, FromValue> getterFrom,
                Function<To, ToValue> getterTo
        ) {
            entries.add(new Entry<>(codec, getterFrom, getterTo));
            return this;
        }

        public CompositeBufferCodec<From, To> build(Function<SimpleByteBuf, From> from, Function<SimpleByteBuf, To> to) {
            return new CompositeBufferCodec<>(fromClass, toClass, entries, from, to);
        }
    }

}
