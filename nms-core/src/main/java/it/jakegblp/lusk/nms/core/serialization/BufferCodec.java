package it.jakegblp.lusk.nms.core.serialization;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface BufferCodec<T> {

    default boolean test(Class<?> type) {
        return key().isSimple() && key().type().equals(type);
    }

    default boolean testOptional(Class<?> type) {
        return key().isOptional() && key().type().isAssignableFrom(type);
    }

    default boolean testList(Class<?> type) {
        return key().isList() && key().type().isAssignableFrom(type);
    }

    TypeKey<T> key();

    default Class<T> type() {
        return key().type();
    }

    void write(SimpleByteBuf buf, @NotNull T value);

    T read(SimpleByteBuf buf);

    default int variant() {
        return key().variant();
    }

    @Contract(value = "_, _ -> new", pure = true)
    static <T extends BufferSerializable<T>> @NotNull BufferCodec<T> of(TypeKey<T> key, Function<SimpleByteBuf, T> reader) {
        return of(key, (BiConsumer<SimpleByteBuf, T>) (buf, t) -> t.write(buf), reader);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static <T extends BufferSerializable<T>> @NotNull BufferCodec<T> of(Class<T> type, Function<SimpleByteBuf, T> reader) {
        return of(type, (BiConsumer<SimpleByteBuf, T>) (buf, t) -> t.write(buf), reader);
    }

    @Contract("_, _, _, _ -> new")
    static <F, T> @NotNull BufferCodec<T> map(TypeKey<T> key, @NotNull Mapper<F, T> mapper, BiConsumer<SimpleByteBuf, @NotNull F> writer, Function<SimpleByteBuf, F> reader) {
        return of(key, (BiConsumer<SimpleByteBuf, T>) (buf, t) -> writer.accept(buf, mapper.from(t)), (Function<SimpleByteBuf, T>) buf -> mapper.to(reader.apply(buf)));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static <T> @NotNull BufferCodec<T> of(TypeKey<T> key, BiConsumer<SimpleByteBuf, @NotNull T> writer, Function<SimpleByteBuf, T> reader) {
        return new BufferCodec<>() {

            @Override
            public TypeKey<T> key() {
                return key;
            }

            @Override
            public void write(SimpleByteBuf buf, @NotNull T value) {
                Preconditions.checkNotNull(value);
                writer.accept(buf, value);
            }

            @Override
            public T read(SimpleByteBuf buf) {
                return Preconditions.checkNotNull(reader.apply(buf));
            }

        };
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static <T> @NotNull BufferCodec<T> of(Class<T> type, BiConsumer<SimpleByteBuf, @NotNull T> writer, Function<SimpleByteBuf, T> reader) {
        return new BufferCodec<>() {

            @Override
            public TypeKey<T> key() {
                return TypeKey.simple(type);
            }

            @Override
            public void write(SimpleByteBuf buf, @NotNull T value) {
                Preconditions.checkNotNull(value);
                writer.accept(buf, value);
            }

            @Override
            public T read(SimpleByteBuf buf) {
                return Preconditions.checkNotNull(reader.apply(buf));
            }

        };
    }

    default BufferCodec<T> withVariant(int variant) {
        return new BufferCodec<>() {
            @Override
            public TypeKey<T> key() {
                return TypeKey.withVariant(BufferCodec.this.key(), variant);
            }

            @Override
            public void write(SimpleByteBuf buf, @NotNull T value) {
                BufferCodec.this.write(buf, value);
            }

            @Override
            public T read(SimpleByteBuf buf) {
                return BufferCodec.this.read(buf);
            }
        };
    }

    @Contract(value = "-> new", pure = true)
    default @NotNull BufferCodec<Optional<T>> optional() {
        return new BufferCodec<>() {

            @Override
            public OptionalKey<T> key() {
                return TypeKey.optional(BufferCodec.this.key());
            }

            @Override
            public void write(SimpleByteBuf buf, @NotNull Optional<T> value) {
                buf.writeBoolean(value.isPresent());
                value.ifPresent(t -> BufferCodec.this.write(buf, t));
            }

            @Override
            public Optional<T> read(SimpleByteBuf buf) {
                return buf.readBoolean()
                        ? Optional.of(BufferCodec.this.read(buf))
                        : Optional.empty();
            }
        };
    }
    @Contract(value = "-> new", pure = true)
    default @NotNull BufferCodec<List<T>> list() {
        return new BufferCodec<>() {

            @Override
            public ListKey<T> key() {
                return TypeKey.list(BufferCodec.this.key());
            }

            @Override
            public void write(SimpleByteBuf buf, @NotNull List<T> value) {
                buf.writeVarInt(value.size());
                for (T t : value)
                    BufferCodec.this.write(buf, t);
            }

            @Override
            public List<T> read(SimpleByteBuf buf) {
                int size = buf.readVarInt();
                List<T> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++)
                    list.add(BufferCodec.this.read(buf));
                return list;
            }
        };
    }

}