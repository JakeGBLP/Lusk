package it.jakegblp.lusk.nms.impl.allversions;

import it.jakegblp.lusk.nms.core.serialization.RegistryMapper;
import it.jakegblp.lusk.nms.core.serialization.TypeKey;
import it.jakegblp.lusk.nms.core.serialization.WrapperKey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.NotNull;

public record RegistryMapperImpl<F, T extends Keyed>(Class<F> fromClass, TypeKey<T> key, ResourceKey<Registry<F>> registryKey) implements RegistryMapper<F, T> {

    public RegistryMapperImpl(Class<F> fromClass, Class<T> toClass, ResourceKey<Registry<F>> registryKey) {
        this(fromClass, TypeKey.simple(toClass), registryKey);
    }

    @Override
    public T to(@NotNull F from) {
        return CraftRegistry.minecraftToBukkit(from, registryKey);
    }

    @Override
    public F from(@NotNull T to) {
        return CraftRegistry.bukkitToMinecraft(to);
    }

    @Override
    public int variant() {
        return key.variant();
    }

    @Override
    public @NotNull RegistryMapperImpl<F, T> withVariant(int variant) {
        return new RegistryMapperImpl<>(fromClass, TypeKey.withVariant(key, variant), registryKey);
    }

    @Override
    public Class<T> toClass() {
        TypeKey<?> localKey = key;
        while (localKey instanceof WrapperKey<?> wrapperKey)
            localKey = wrapperKey.inner();
        return (Class<T>) localKey.type();
    }
}