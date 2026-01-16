package it.jakegblp.lusk.nms.core.util;

import it.jakegblp.lusk.nms.core.world.entity.serialization.DataHolderType;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NullMarked
public class CompositeBufferCodec<From, To> implements SimpleBufferCodec<From, To> {

    private final Class<From> fromClass;
    private final Class<To> toClass;
    private final List<Entry<From, To, ?, ?>> entries;
    private final Function<SimpleByteBuf, From> creatorFrom;
    private final Function<SimpleByteBuf, To> creatorTo;

    protected CompositeBufferCodec(
            Class<From> fromClass, Class<To> toClass,
            List<Entry<From, To, ?, ?>> entries,
            Function<SimpleByteBuf, From> creatorFrom,
            Function<SimpleByteBuf, To> creatorTo
    ) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.entries = List.copyOf(entries);
        this.creatorFrom = creatorFrom;
        this.creatorTo = creatorTo;
    }

    public static <From, To> SimpleBuilder<From, To> builder(Class<From> fromClass, Class<To> toClass) {
        return new SimpleBuilder<>(fromClass, toClass);
    }

    public static <From, To> SimpleBuilder<From, To> builder(Class<From> fromClass, Function<From, SimpleByteBuf> toByteBuffer, Class<To> toClass) {
        return new SimpleBuilder<>(fromClass, toClass);
    }

    @Override
    public Class<From> getFromClass() {
        return fromClass;
    }

    @Override
    public Class<To> getToClass() {
        return toClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeFrom(From from, SimpleByteBuf buffer) {
        for (Entry<From, To, ?, ?> entry : entries) {
            Object part = entry.getterFrom().apply(from);
            SimpleBufferCodec<Object, Object> codec = (SimpleBufferCodec<Object, Object>) entry.codec();
            codec.writeFrom(part, buffer);
        }
    }

    @Override
    public From readFrom(SimpleByteBuf buffer) {
        return creatorFrom.apply(buffer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeTo(To to, SimpleByteBuf buffer) {
        for (Entry<From, To, ?, ?> entry : entries) {
            Object part = entry.getterTo().apply(to);
            SimpleBufferCodec<Object, Object> codec = (SimpleBufferCodec<Object, Object>) entry.codec();
            codec.writeTo(part, buffer);
        }
    }

    @Override
    public To readTo(SimpleByteBuf buffer) {
        return creatorTo.apply(buffer);
    }

    @Override
    public DataHolderType getHolderType() {
        return DataHolderType.NORMAL;
    }

    public record Entry<From, To, FromValue, ToValue>(
            BufferCodec codec,
            Function<From, FromValue> getterFrom,
            Function<To, ToValue> getterTo
    ) {
    }

    public static final class SimpleBuilder<From, To> {
        private final Class<From> fromClass;
        private final Class<To> toClass;
        private final List<Entry<From, To, ?, ?>> entries = new ArrayList<>();

        public SimpleBuilder(Class<From> fromClass, Class<To> toClass) {
            this.fromClass = fromClass;
            this.toClass = toClass;
        }

        public <FromValue, ToValue> SimpleBuilder<From, To> with(
                SimpleBufferCodec<FromValue, ToValue> codec,
                Function<From, FromValue> getterFrom,
                Function<To, ToValue> getterTo
        ) {
            entries.add(new Entry<>(codec, getterFrom, getterTo));
            return this;
        }

        public CompositeBufferCodec<From, To> build(
                Function<SimpleByteBuf, From> constructorFrom,
                Function<SimpleByteBuf, To> constructorTo
        ) {
            return new CompositeBufferCodec<>(fromClass, toClass, entries, constructorFrom, constructorTo);
        }
    }
}
