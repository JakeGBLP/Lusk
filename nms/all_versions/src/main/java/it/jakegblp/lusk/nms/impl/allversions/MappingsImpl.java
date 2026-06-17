package it.jakegblp.lusk.nms.impl.allversions;

import it.jakegblp.lusk.nms.core.serialization.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class MappingsImpl implements Mappings {
    private final List<Mapper<?, ?>> mappers = new ArrayList<>();

    private final AllVersions allVersions;

    public MappingsImpl(AllVersions allVersions) {
        this.allVersions = allVersions;
    }

    @Contract(pure = true)
    @Override
    public @UnmodifiableView @NotNull List<Mapper<?, ?>> getMappers() {
        return Collections.unmodifiableList(mappers);
    }

    @Contract(pure = true)
    @Override
    public <F, T> Mapper<F, T> register(
            Class<F> fromClass,
            Class<T> toClass,
            Function<@NotNull F, T> to,
            Function<@NotNull T, F> from
    ) {
        return register(Mapper.of(fromClass, toClass, to, from, 0));
    }

    @Contract(pure = true)
    @Override
    public <F, T> BufferCodecMapper<F, T> register(
            Class<F> fClass,
            BiConsumer<SimpleByteBuf, F> writer, Function<SimpleByteBuf, F> reader,
            BufferCodec<T> toBufferCodec
    ) {
        return register(BufferCodecMapper.of(BufferCodec.of(fClass, writer, reader), toBufferCodec, 0));
    }

    @Contract(pure = true)
    public <F, T> BufferCodecMapper<F, T> register(
            Class<F> fClass,
            StreamCodec<FriendlyByteBuf, F> streamCodec,
            BufferCodec<T> toBufferCodec
    ) {
        return register(BufferCodecMapper.of(BufferCodec.of(fClass, (buf, f) -> streamCodec.encode(allVersions.toFriendlyByteBuf(buf), f), buf -> streamCodec.decode(allVersions.toFriendlyByteBuf(buf))), toBufferCodec, 0));
    }

    @Override
    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    public <F, T> BufferCodecMapper<F, T> registerStreamCodecUnsafe(
            Class<F> fClass,
            Object streamCodec,
            BufferCodec<T> toBufferCodec
    ) {
        return register(fClass, (StreamCodec<FriendlyByteBuf, F>) streamCodec, toBufferCodec);
    }

    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    @Override
    public <M extends Mapper<F, T>, F, T> M register(@NotNull M mapper) {
        Class<F> from = mapper.fromClass();
        Class<T> to = mapper.toClass();
        int variant = mapper.variant();

        if (exists(from, to, variant)) {
            variant = 0;
            while (exists(from, to, variant))
                variant++;
        }

        M finalMapper = (M) mapper.withVariant(variant);
        mappers.add(finalMapper);
        return finalMapper;
    }

    @Contract(pure = true)
    @Override
    public <F extends Enum<F>, T extends Enum<T>> EnumMapper<F, T> register(Class<F> from, Class<T> to) {
        return register(EnumMapper.of(from, to));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F, T extends Keyed> RegistryMapper<F, T> registerRegistryUnsafe(Class<F> from, Class<T> to, Object nmsRegistry) {
        return register(from, to, (ResourceKey<Registry<F>>) nmsRegistry);
    }

    @Contract(pure = true)
    public <F, T extends Keyed> RegistryMapper<F, T> register(Class<F> from, Class<T> to, ResourceKey<Registry<F>> nmsRegistry) {
        return register(new RegistryMapperImpl<>(from, to, nmsRegistry));
    }

    private <F, T> boolean exists(Class<F> from, Class<T> to, int variant) {
        for (Mapper<?, ?> mapper : mappers)
            if (mapper.fromClass() == from && mapper.toClass() == to && mapper.variant() == variant)
                return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F, T> T map(F f) {
        if (f == null) return null;
        if (f instanceof Holder<?> holder)
            f = (F) holder.value();
        Class<F> type = (Class<F>) f.getClass();
        if (allVersions.isSimpleType(type)) return (T) f;
        for (Mapper mapper : mappers) {
            //System.out.println("map: " + mapper + ", from: " + mapper.fromClass() + ", to: " + mapper.toClass() + ", value: " + f);
            if (mapper.fromClass() == type)
                return (T) mapper.to(f);
        }
        throw new IllegalArgumentException("No forward mapping implementation found for type " + type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, F> F mapBack(T t) {
        if (t == null) return null;
        var type = t.getClass();
        if (allVersions.isSerializable(type)) return (F) t;
        for (Mapper<?, ?> mapper : mappers)
            if (mapper.toClass().isAssignableFrom(type))
                return ((Mapper<F, T>)mapper).from(t);
        throw new IllegalArgumentException("No backward mapping implementation found for type " + type);

    }
}
