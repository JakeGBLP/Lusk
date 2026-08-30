package it.jakegblp.lusk.nms.impl.allversions;

import it.jakegblp.lusk.nms.core.serialization.RegistryMapper;
import it.jakegblp.lusk.nms.core.serialization.TypeKey;
import it.jakegblp.lusk.nms.core.serialization.WrapperKey;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RegistryMapperImpl<F, T extends Keyed>(Class<F> fromClass, TypeKey<T> key, ResourceKey<Registry<F>> registryKey) implements RegistryMapper<F, T> {

    public RegistryMapperImpl(Class<F> fromClass, Class<T> toClass, ResourceKey<Registry<F>> registryKey) {
        this(fromClass, TypeKey.simple(toClass), registryKey);
    }

    @Override
    public T to(F from) {
        return CraftRegistry.minecraftToBukkit(from, registryKey);
    }

    public T to(Holder<F> holder) {
        return CraftRegistry.minecraftHolderToBukkit(holder, registryKey);
    }

    @Override
    public F from(T to) {
        return CraftRegistry.bukkitToMinecraft(to);
    }

    @Override
    public int variant() {
        return key.variant();
    }

    @Override
    public RegistryMapperImpl<F, T> withVariant(int variant) {
        return new RegistryMapperImpl<>(fromClass, TypeKey.withVariant(key, variant), registryKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> toClass() {
        TypeKey<?> localKey = key;
        while (localKey instanceof WrapperKey<?> wrapperKey)
            localKey = wrapperKey.inner();
        return (Class<T>) localKey.type();
    }
}