package it.jakegblp.lusk.nms.core.world.entity.metadata.flags;

import com.google.common.base.Preconditions;
import it.jakegblp.lusk.common.PseudoEnum;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;

@NullMarked
@Getter
public abstract class Flag<H extends Entity, T, F extends Flag<H, T ,F>> extends PseudoEnum<F> {

    public static <H extends Entity, T> MappedBooleanFlag<H, T> mappedBooleanFlag(
            String name,
            Class<T> type,
            Function<H, T> getter,
            BiConsumer<H, T> setter,
            Function<T, Boolean> encoder,
            Function<Boolean, T> decoder,
            T defaultValue
    ) {
        return new MappedBooleanFlag<>(name, defaultValue) {

            @Override
            protected boolean toBoolean(T value) {
                return encoder.apply(value);
            }

            @Override
            protected T fromBoolean(boolean value) {
                return decoder.apply(value);
            }

            @Override
            public T get(H holder) {
                return getter.apply(holder);
            }

            @Override
            public void setInternal(H holder, T value) {
                setter.accept(holder, value);
            }

            @Override
            public Class<T> getValueClass() {
                return type;
            }
        };
    }

    public static <H extends Entity, T> MappedIntFlag<H, T> mappedIntFlag(
            String name,
            int width,
            Class<T> type,
            Function<H, T> getter,
            BiConsumer<H, T> setter,
            Function<T, Integer> encoder,
            IntFunction<T> decoder,
            T defaultValue
    ) {
        return new MappedIntFlag<>(name, defaultValue, width) {

            @Override
            protected int toBits(T value) {
                return encoder.apply(value);
            }

            @Override
            protected T fromBits(int bits) {
                return decoder.apply(bits);
            }

            @Override
            public T get(H holder) {
                return getter.apply(holder);
            }

            @Override
            public void setInternal(H holder, T value) {
                setter.accept(holder, value);
            }

            @Override
            public Class<T> getValueClass() {
                return type;
            }
        };
    }

    public static <H extends Entity, E extends Enum<E>> EnumFlag<H, E> enumFlag(String name, Class<E> type, Function<H, E> getter, BiConsumer<H, E> setter, E defaultValue) {
        return new EnumFlag<>(name, type, defaultValue) {
            @Override
            public E get(H holder) {
                return getter.apply(holder);
            }

            @Override
            public void setInternal(H holder, E value) {
                setter.accept(holder, value);
            }
        };
    }

    public static <H extends Entity, E extends Enum<E>> EnumFlag<H, E> enumFlag(String name, Class<E> type, Function<H, E> getter, BiConsumer<H, E> setter) {
        return new EnumFlag<>(name, type) {
            @Override
            public E get(H holder) {
                return getter.apply(holder);
            }

            @Override
            public void setInternal(H holder, E value) {
                setter.accept(holder, value);
            }
        };
    }

    public static <H extends Entity> BooleanFlag<H> booleanFlag(String name, Function<H, Boolean> getter, BiConsumer<H, Boolean> setter, Boolean defaultValue) {
        return new BooleanFlag<>(name, defaultValue) {
            @Override
            public Boolean get(H holder) {
                return getter.apply(holder);
            }

            @Override
            public void setInternal(H holder, Boolean value) {
                setter.accept(holder, value);
            }
        };
    }

    public static <H extends Entity> BooleanFlag<H> booleanFlag(String name, Function<H, Boolean> getter, BiConsumer<H, Boolean> setter) {
        return new BooleanFlag<>(name) {
            @Override
            public Boolean get(H holder) {
                return getter.apply(holder);
            }

            @Override
            public void setInternal(H holder, Boolean value) {
                setter.accept(holder, value);
            }
        };
    }

    public static <H extends Entity> BooleanFlag<H> unusedBooleanFlag(String name) {
        return new BooleanFlag<>(name) {
            @Override
            public Boolean get(H holder) {
                throw new IllegalStateException("Tried to get value for unused flag");
            }

            @Override
            public void setInternal(H holder, Boolean value) {
                throw new IllegalStateException("Tried to set value for unused flag");
            }
        };
    }

    public static <H extends Entity, M> BooleanFlag<H> complexBooleanFlag(String name, Function<H, M> midGetter, BiConsumer<H, M> midSetter, Function<M, Boolean> getter, BiConsumer<M, Boolean> setter) {
        return new BooleanFlag<>(name) {
            @Override
            public Boolean get(H holder) {
                return getter.apply(midGetter.apply(holder));
            }

            @Override
            public void setInternal(H holder, Boolean value) {
                M mid = midGetter.apply(holder);
                setter.accept(mid, value);
                midSetter.accept(holder, mid);
            }
        };
    }

    public static <H extends Entity> BooleanFlag<H> invertedBooleanFlag(String name, Function<H, Boolean> getter, BiConsumer<H, Boolean> setter) {
        return new BooleanFlag<>(name) {
            @Override
            public Boolean get(H holder) {
                return !getter.apply(holder);
            }

            @Override
            public void setInternal(H holder, Boolean value) {
                setter.accept(holder, !value);
            }
        };
    }

    private final int width, shift, mask;
    private final T defaultValue;
    private final boolean used;

    protected Flag(String name, T defaultValue, int width) {
        super(name);
        this.width = width;
        this.shift = previous == null ? 0 : previous.getShift() + previous.getWidth();
        this.mask = ((1 << width) - 1) << shift;
        this.defaultValue = defaultValue;
        this.used = true;
    }

    @SuppressWarnings("ConstantConditions")
    protected Flag(String name) {
        super(name);
        this.width = -1;
        this.shift = -1;
        this.mask = -1;
        this.defaultValue = null;
        this.used = false;
    }

    public final void set(H holder, @Nullable T value) {
        Preconditions.checkArgument(!used, "This flag cannot be used");
        setInternal(holder, value == null ? defaultValue : value);
    }

    public abstract T get(H holder);

    public abstract void setInternal(H holder, T value);

    @Nullable
    public abstract T decode(int bits);

    public abstract int encode(@Nullable T value);

    public abstract Class<T> getValueClass();

}