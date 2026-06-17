package it.jakegblp.lusk.nms.core.world.entity.serialization;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.serialization.BufferCodec;
import org.jspecify.annotations.NullMarked;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@NullMarked
public interface EntityDataSerializer<T> {
    BufferCodec<T> codec();

    default EntityDataAccessor<T> createAccessor(int id) {
        return new EntityDataAccessor<>(id, this);
    }

    static <T> EntityDataSerializer<T> simple(AbstractNMS nms, Class<T> type) {
        return new EntityDataSerializer<>() {
            private final BufferCodec<T> codec = nms.getCodecs().get(type);

            @Override
            public String toString() {
                return "simple EntityDataSerializer{" +
                        "codec=" + codec.key() +
                        '}';
            }

            @Override
            public BufferCodec<T> codec() {
                return codec;
            }

        };
    }

    static <T> EntityDataSerializer<T> optional(AbstractNMS nms, Class<T> type) {
        return new EntityDataSerializer<>() {
            private final BufferCodec<T> codec = nms.getCodecs().getOptional(type);

            @Override
            public String toString() {
                return "optional EntityDataSerializer{" +
                        "codec=" + codec.key() +
                        '}';
            }

            @Override
            public BufferCodec<T> codec() {
                return codec;
            }

        };
    }

    static <T> EntityDataSerializer<T> list(AbstractNMS nms, Class<T> type) {
        return new EntityDataSerializer<>() {
            private final BufferCodec<T> codec = nms.getCodecs().getList(type);

            @Override
            public String toString() {
                return "list EntityDataSerializer{" +
                        "codec=" + codec.key() +
                        '}';
            }

            @Override
            public BufferCodec<T> codec() {
                return codec;
            }

        };
    }

    static <T> EntityDataSerializer<T> holder(AbstractNMS nms, Class<T> type) {
        return new EntityDataSerializer<>() {
            private final BufferCodec<T> codec = nms.getCodecs().getHolder(type);

            @Override
            public String toString() {
                return "holder EntityDataSerializer{" +
                        "codec=" + codec.key() +
                        '}';
            }

            @Override
            public BufferCodec<T> codec() {
                return codec;
            }

        };
    }

    static <F> EntityDataSerializer<F> list(Class<F> toClass) {
        return list(NMS, toClass);
    }

    static <F> EntityDataSerializer<F> simple(Class<F> toClass) {
        return simple(NMS, toClass);
    }

    static <F> EntityDataSerializer<F> optional(Class<F> toClass) {
        return optional(NMS, toClass);
    }

}
