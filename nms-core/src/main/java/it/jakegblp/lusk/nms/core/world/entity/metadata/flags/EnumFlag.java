package it.jakegblp.lusk.nms.core.world.entity.metadata.flags;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class EnumFlag<H extends Entity, E extends Enum<E>> extends Flag<H, E, EnumFlag<H, E>> {

    protected final Class<E> type;

    public static int bitsRequired(Class<? extends Enum<?>> type) {
        int values = type.getEnumConstants().length;
        return Math.max(1, Integer.SIZE - Integer.numberOfLeadingZeros(values - 1));
    }

    public EnumFlag(String name, Class<E> type) {
        this(name, type, type.getEnumConstants()[0]);
    }

    public EnumFlag(String name, Class<E> type, E defaultValue) {
        super(name, defaultValue, bitsRequired(type));
        this.type = type;
    }

    @Override
    public @Nullable E decode(int bits) {
        return getValueClass().getEnumConstants()[bits];
    }

    @Override
    public int encode(@Nullable E value) {
        return (value == null ? getDefaultValue() : value).ordinal();
    }

    @Override
    public Class<E> getValueClass() {
        return type;
    }
}