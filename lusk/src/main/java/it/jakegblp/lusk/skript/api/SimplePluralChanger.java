package it.jakegblp.lusk.skript.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface SimplePluralChanger<C, V> extends GenericSimpleChanger<C, V> {

    @Override
    default boolean isSingle() {
        return false;
    }

    default void set(C change, @NotNull V[] delta) {}
    default void add(C change, @NotNull V[] delta) {}
    default void remove(C change, @NotNull V[] delta) {}
    default void removeAll(C change, @NotNull V[] delta) {}

    static <C, V> Builder<C, V> builder(Class<C> type, Class<V> valueClass, Class<V[]> valueArrayClass) {
        return new Builder<C, V>()
                .valueClass(valueClass)
                .valueArrayClass(valueArrayClass);
    }

    class Builder<C, V> {
        private @Nullable Class<V> valueClass;
        private @Nullable Class<V[]> valueArrayClass;

        private final EnumMap<ChangeMode, Object> handlers = new EnumMap<>(ChangeMode.class);

        public Builder<C, V> valueClass(Class<V> valueClass) {
            this.valueClass = valueClass;
            return this;
        }

        public Builder<C, V> valueArrayClass(Class<V[]> valueArrayClass) {
            this.valueArrayClass = valueArrayClass;
            return this;
        }

        // region --- Change Handlers ---

        public Builder<C, V> add(BiConsumer<C, V[]> handler) {
            return register(ChangeMode.ADD, handler);
        }

        public Builder<C, V> remove(BiConsumer<C, V[]> handler) {
            return register(ChangeMode.REMOVE, handler);
        }

        public Builder<C, V> removeAll(BiConsumer<C, V[]> handler) {
            return register(ChangeMode.REMOVE_ALL, handler);
        }

        public Builder<C, V> set(BiConsumer<C, V[]> handler) {
            return register(ChangeMode.SET, handler);
        }

        public Builder<C, V> reset(Consumer<C> handler) {
            return register(ChangeMode.RESET, handler);
        }

        public Builder<C, V> delete(Consumer<C> handler) {
            return register(ChangeMode.DELETE, handler);
        }

        private Builder<C, V> register(ChangeMode mode, Object handler) {
            handlers.put(mode, handler);
            return this;
        }

        // endregion

        public SimplePluralChanger<C, V> build() {
            Objects.requireNonNull(valueClass, "valueClass must be set");
            Objects.requireNonNull(valueArrayClass, "valueArrayClass must be set");

            ChangeMode[] allowedModes = handlers.keySet().toArray(ChangeMode[]::new);

            return new SimplePluralChanger<>() {
                @Override
                public Class<V> getValueClass() {
                    return valueClass;
                }

                @Override
                public Class<V[]> getValueArrayClass() {
                    return valueArrayClass;
                }

                @Override
                public ChangeMode[] getAllowedChangeModes() {
                    return allowedModes;
                }

                @Override
                @SuppressWarnings("unchecked")
                public void set(C change, V[] delta) {
                    ((BiConsumer<C, V[]>) handlers.get(ChangeMode.SET)).accept(change, delta);
                }

                @Override
                @SuppressWarnings("unchecked")
                public void add(C change, V[] delta) {
                    ((BiConsumer<C, V[]>) handlers.get(ChangeMode.ADD)).accept(change, delta);
                }

                @Override
                @SuppressWarnings("unchecked")
                public void remove(C change, V[] delta) {
                    ((BiConsumer<C, V[]>) handlers.get(ChangeMode.REMOVE)).accept(change, delta);
                }

                @Override
                @SuppressWarnings("unchecked")
                public void removeAll(C change, V[] delta) {
                    ((BiConsumer<C, V[]>) handlers.get(ChangeMode.REMOVE_ALL)).accept(change, delta);
                }

                @Override
                @SuppressWarnings("unchecked")
                public void delete(C change) {
                    ((Consumer<C>) handlers.get(ChangeMode.DELETE)).accept(change);
                }

                @Override
                @SuppressWarnings("unchecked")
                public void reset(C change) {
                    ((Consumer<C>) handlers.get(ChangeMode.RESET)).accept(change);
                }
            };
        }
    }
}
