package it.jakegblp.lusk.nms.core.serialization;

import org.bukkit.Keyed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface BufferCodecs {

    @Contract(pure = true)
    @UnmodifiableView @NotNull List<BufferCodec<?>> getBufferCodecs();

    <T> BufferCodec<T> get(Class<T> type);

    <T> BufferCodec<T> getOptional(Class<T> type);

    <T> BufferCodec<T> getList(Class<T> type);

    <T> BufferCodec<T> getHolder(Class<T> type);

    @Contract(value = "_, _, _ -> new", pure = true)
    <T> @NotNull BufferCodec<T> register(Class<T> type, BiConsumer<SimpleByteBuf, T> writer, Function<SimpleByteBuf, T> reader);

    @Contract("_, _, _ -> new")
    <F, T> @NotNull BufferCodec<T> register(Mapper<F, T> mapper, BiConsumer<SimpleByteBuf, F> writer, Function<SimpleByteBuf, F> reader);

    <T extends Enum<T>> @NotNull EnumBufferCodec<T> register(Class<T> type);

    <B extends BufferCodec<T>, T> @NotNull B register(@NotNull B bufferCodec);

    <F, T extends Keyed> @NotNull BufferCodec<T> registerUnsafe(RegistryMapper<F, T> mapper, Object nmsRegistryStreamCodec);

    <T> void encode(SimpleByteBuf buffer, T value, TypeKey<T> key);

    <T> T decode(SimpleByteBuf buffer, TypeKey<T> key);
}
