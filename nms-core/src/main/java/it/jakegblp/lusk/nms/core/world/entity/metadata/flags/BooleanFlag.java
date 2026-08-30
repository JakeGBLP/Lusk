package it.jakegblp.lusk.nms.core.world.entity.metadata.flags;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BooleanFlag<H extends Entity> extends Flag<H, Boolean, BooleanFlag<H>> {

    public BooleanFlag(String name) {
        this(name, false);
    }

    public BooleanFlag(String name, boolean defaultValue) {
        super(name, defaultValue, 1);
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public @Nullable Boolean decode(int bits) {
        return bits != 0;
    }

    @Override
    public int encode(@Nullable Boolean value) {
        return (value == null ? getDefaultValue() : value) ? 1 : 0;
    }

    @Override
    public Class<Boolean> getValueClass() {
        return Boolean.class;
    }

}