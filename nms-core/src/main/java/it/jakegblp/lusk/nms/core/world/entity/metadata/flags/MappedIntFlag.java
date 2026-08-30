package it.jakegblp.lusk.nms.core.world.entity.metadata.flags;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class MappedIntFlag<H extends Entity, T> extends Flag<H, T, MappedIntFlag<H, T>> {

    public MappedIntFlag(String name, T defaultValue, int width) {
        super(name, defaultValue, width);
    }

    protected abstract int toBits(T value);

    protected abstract T fromBits(int bits);

    @Override
    public @Nullable T decode(int bits) {
        return fromBits(bits);
    }

    @Override
    public int encode(@Nullable T value) {
        return toBits(value == null ? getDefaultValue() : value);
    }
}