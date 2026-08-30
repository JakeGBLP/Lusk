package it.jakegblp.lusk.nms.core.world.entity.metadata.flags;

import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class MappedBooleanFlag<H extends Entity, T> extends Flag<H, T, MappedBooleanFlag<H, T>> {

    public MappedBooleanFlag(String name, T defaultValue) {
        super(name, defaultValue, 1);
    }

    @Override
    public int getWidth() {
        return 1;
    }

    protected abstract boolean toBoolean(T value);

    protected abstract T fromBoolean(boolean value);

    @Override
    public @Nullable T decode(int bits) {
        return fromBoolean(bits != 0);
    }

    @Override
    public int encode(@Nullable T value) {
        return toBoolean(value == null ? getDefaultValue() : value) ? 1 : 0;
    }
}