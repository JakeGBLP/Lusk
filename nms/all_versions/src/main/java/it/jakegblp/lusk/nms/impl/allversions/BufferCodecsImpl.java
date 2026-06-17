package it.jakegblp.lusk.nms.impl.allversions;

import io.netty.buffer.ByteBuf;
import it.jakegblp.lusk.nms.core.serialization.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class BufferCodecsImpl implements BufferCodecs {

    private final Map<TypeKey<?>, BufferCodec<?>> bufferCodecs = new HashMap<>();
    private final AllVersions allVersions;

    public BufferCodecsImpl(AllVersions allVersions) {
        this.allVersions = allVersions;
    }

    @Override
    @Contract(pure = true)
    public @UnmodifiableView @NotNull List<BufferCodec<?>> getBufferCodecs() {
        return List.copyOf(bufferCodecs.values());
    }

    @Override
    @SuppressWarnings("unchecked")
    @NotNull
    public <T> BufferCodec<T> get(Class<T> type) {
        var codec = (BufferCodec<T>) bufferCodecs.get(TypeKey.simple(type));
        if (codec == null)
            throw new IllegalArgumentException("No simple codec found for type " + type);
        return codec;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> BufferCodec<T> getOptional(Class<T> type) {
        var key = TypeKey.optional(type);
        var codec = (BufferCodec<T>) bufferCodecs.get(key);
        if (codec == null) {
            throw new IllegalArgumentException("No optional codec found for type " + type);
        }
        return codec;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> BufferCodec<T> getList(Class<T> type) {
        var codec = (BufferCodec<T>) bufferCodecs.get(TypeKey.list(type));
        if (codec == null) {
            throw new IllegalArgumentException("No list codec found for type " + type);
        }
        return codec;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> BufferCodec<T> getHolder(Class<T> type) {
        var codec = (BufferCodec<T>) bufferCodecs.get(TypeKey.holder(type));
        if (codec == null) {
            throw new IllegalArgumentException("No holder codec found for type " + type);
        }
        return codec;
    }

    @Override
    @Contract(value = "_, _, _ -> new", pure = true)
    public <T> @NotNull BufferCodec<T> register(Class<T> type, BiConsumer<SimpleByteBuf, T> writer, Function<SimpleByteBuf, T> reader) {
        return register(BufferCodec.of(TypeKey.simple(type), writer, reader));
    }

    @Override
    @Contract("_, _, _ -> new")
    public <F, T> @NotNull BufferCodec<T> register(Mapper<F, T> mapper, BiConsumer<SimpleByteBuf, F> writer, Function<SimpleByteBuf, F> reader) {
        return register(BufferCodec.map(TypeKey.simple(mapper.toClass()), mapper, writer, reader));
    }

    @Override
    public <T extends Enum<T>> @NotNull EnumBufferCodec<T> register(Class<T> type) {
        return register(new EnumBufferCodec<>(type));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B extends BufferCodec<T>, T> @NotNull B register(@NotNull B bufferCodec) {
        int variant = bufferCodec.key().variant();
        TypeKey<T> key = bufferCodec.key();

        if (exists(key)) {
            variant = 0;
            while (exists(TypeKey.withVariant(key, variant)))
                variant++;
        }

        B finalCodec = (B) bufferCodec.withVariant(variant);
        bufferCodecs.put(finalCodec.key(), finalCodec);
        return finalCodec;
    }

    public <F, T extends Keyed> @NotNull BufferCodec<T> register(RegistryMapper<F, T> mapper, StreamCodec<RegistryFriendlyByteBuf, Holder<F>> streamCodec) {
        Registry<F> registry = CraftRegistry.getMinecraftRegistry(((RegistryMapperImpl<F, T>) mapper).registryKey());
        return register(BufferCodec.map(TypeKey.holder(mapper.toClass()), mapper, (buf, f) -> streamCodec.encode(allVersions.toRegistryFriendlyByteBuf(buf), registry.getOrThrow(registry.getResourceKey(f).orElseThrow())), buf -> streamCodec.decode(allVersions.toRegistryFriendlyByteBuf(buf)).value()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <F, T extends Keyed> BufferCodec<T> registerUnsafe(RegistryMapper<F, T> mapper, Object nmsRegistryStreamCodec) {
        return register(mapper, (StreamCodec<RegistryFriendlyByteBuf, Holder<F>>) nmsRegistryStreamCodec);
    }

    public <F, T> @NotNull BufferCodec<T> register(Mapper<F, T> mapper, StreamCodec<ByteBuf, F> streamCodec) {
        return register(mapper, (buf, f) -> streamCodec.encode(buf.unwrap(), f), buf -> streamCodec.decode(buf.unwrap()));
    }

    private boolean exists(TypeKey<?> codecKey) {
        return bufferCodecs.containsKey(codecKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void encode(SimpleByteBuf buffer, T value, TypeKey<T> key) {
        BufferCodec<T> codec = (BufferCodec<T>) bufferCodecs.get(key);

        if (codec == null)
            throw new IllegalArgumentException("No BufferCodec found for key " + key);

        codec.write(buffer, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T decode(SimpleByteBuf buffer, TypeKey<T> key) {
        BufferCodec<T> codec = (BufferCodec<T>) bufferCodecs.get(key);

        if (codec == null)
            throw new IllegalArgumentException("No BufferCodec found for key " + key);

        return codec.read(buffer);
    }
}