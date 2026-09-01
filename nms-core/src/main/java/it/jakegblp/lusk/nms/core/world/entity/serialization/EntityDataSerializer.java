package it.jakegblp.lusk.nms.core.world.entity.serialization;

import it.jakegblp.lusk.nms.core.serialization.codec.BufferCodec;
import it.jakegblp.lusk.nms.core.serialization.codec.BufferCodecs;
import it.jakegblp.lusk.nms.core.serialization.key.*;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EntityDataSerializer<T, K extends TypeKey<?>> {

    static <T> Simple<T> simple(BufferCodecs bufferCodecs, Class<T> type) {
        return new Simple<>(bufferCodecs, type);
    }

    static <T> Optional<T> optional(BufferCodecs bufferCodecs, Class<T> type) {
        return new Optional<>(bufferCodecs, type);
    }

    static <T> List<T> list(BufferCodecs bufferCodecs, Class<T> type) {
        return new List<>(bufferCodecs, type);
    }

    static <T> Holder<T> holder(BufferCodecs bufferCodecs, Class<T> type) {
        return new Holder<>(bufferCodecs, type);
    }


    BufferCodec<T> codec();

    default EntityDataAccessor<T> createAccessor(int id) {
        return new EntityDataAccessor<>(id, this);
    }

    K key();

    @ToString
    final class Simple<T> implements EntityDataSerializer<T, SimpleKey<T>> {
        private final BufferCodec<T> codec;

        private Simple(BufferCodecs bufferCodecs, Class<T> type) {
            this.codec = bufferCodecs.get(type);
        }
        @Override
        public BufferCodec<T> codec() {
            return codec;
        }

        @Override
        public SimpleKey<T> key() {
            return (SimpleKey<T>) codec.key();
        }
    }

    @ToString
    final class Optional<T> implements EntityDataSerializer<T, OptionalKey<T>> {
        private final BufferCodec<T> codec;

        private Optional(BufferCodecs bufferCodecs, Class<T> type) {
            this.codec = bufferCodecs.getOptional(type);
        }

        @Override
        public BufferCodec<T> codec() {
            return codec;
        }

        @Override
        @SuppressWarnings("unchecked")
        public OptionalKey<T> key() {
            return (OptionalKey<T>) codec.key();
        }
    }

    @ToString
    final class List<T> implements EntityDataSerializer<T, ListKey<T>> {
        private final BufferCodec<T> codec;

        private List(BufferCodecs bufferCodecs, Class<T> type) {
            this.codec = bufferCodecs.getList(type);
        }

        @Override
        public BufferCodec<T> codec() {
            return codec;
        }

        @Override
        @SuppressWarnings("unchecked")
        public ListKey<T> key() {
            return (ListKey<T>) codec.key();
        }
    }

    @ToString
    final class Holder<T> implements EntityDataSerializer<T, HolderKey<T>> {
        private final BufferCodec<T> codec;

        private Holder(BufferCodecs bufferCodecs, Class<T> type) {
            this.codec = bufferCodecs.getHolder(type);
        }

        @Override
        public BufferCodec<T> codec() {
            return codec;
        }

        @Override
        public HolderKey<T> key() {
            return (HolderKey<T>) codec.key();
        }
    }

}
