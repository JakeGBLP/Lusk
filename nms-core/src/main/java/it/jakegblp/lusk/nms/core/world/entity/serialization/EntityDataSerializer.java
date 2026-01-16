package it.jakegblp.lusk.nms.core.world.entity.serialization;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.util.IdentityBufferCodec;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import it.jakegblp.lusk.nms.core.util.SimpleBufferCodec;
import org.jspecify.annotations.NullMarked;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@NullMarked
public interface EntityDataSerializer<F> {
    SimpleBufferCodec<F, ?> codec();

    default EntityDataAccessor<F> createAccessor(int id) {
        return new EntityDataAccessor<>(id, this);
    }

    F copy(F var1);

    DataHolderType getHolderType();

    static <F> EntityDataSerializer<F> simple(AbstractNMS nms, Class<F> fromClass) {
        return new EntityDataSerializer<>() {
            private final SimpleBufferCodec<F, ?> codec = nms.getFirstNMSCodec(fromClass);

            @Override
            public SimpleBufferCodec<F, ?> codec() {
                return codec;
            }
            @Override
            @SuppressWarnings("unchecked")
            public F copy(F var1) {
                return codec instanceof IdentityBufferCodec<?> identityBufferCodec ? ((IdentityBufferCodec<F>)identityBufferCodec).encode(var1) : NullabilityUtils.copyIfNotNull(var1);
            }

            @Override
            public DataHolderType getHolderType() {
                return DataHolderType.NORMAL;
            }
        };
    }

    static <F> EntityDataSerializer<F> optional(AbstractNMS nms, Class<F> fromClass) {
        return new EntityDataSerializer<>() {
            private final SimpleBufferCodec<F, ?> codec = nms.getFirstNMSCodec(fromClass);

            @Override
            public SimpleBufferCodec<F, ?> codec() {
                return codec;
            }

            @Override
            @SuppressWarnings("unchecked")
            public F copy(F var1) {
                // todo: specialized codec? (initial boolean chcek)
                return codec instanceof IdentityBufferCodec<?> identityBufferCodec ? ((IdentityBufferCodec<F>)identityBufferCodec).encode(var1) : NullabilityUtils.copyIfNotNull(var1);
            }

            @Override
            public DataHolderType getHolderType() {
                return DataHolderType.OPTIONAL;
            }
        };
    }

    static <F> EntityDataSerializer<F> list(AbstractNMS nms, Class<F> fromClass) {
        return new EntityDataSerializer<>() {
            private final SimpleBufferCodec<F, ?> codec = nms.getFirstNMSCodec(fromClass);

            @Override
            public SimpleBufferCodec<F, ?> codec() {
                return codec;
            }

            @Override
            @SuppressWarnings("unchecked")
            public F copy(F var1) {
                // todo: specialized codec? (initial boolean chcek)
                return codec instanceof IdentityBufferCodec<?> identityBufferCodec ? ((IdentityBufferCodec<F>)identityBufferCodec).encode(var1) : NullabilityUtils.copyIfNotNull(var1);
            }

            @Override
            public DataHolderType getHolderType() {
                return DataHolderType.LIST;
            }
        };
    }

    static <F> EntityDataSerializer<F> holder(AbstractNMS nms, Class<F> fromClass) {
        return new EntityDataSerializer<>() {
            private final SimpleBufferCodec<F, ?> codec = nms.getFirstNMSCodec(fromClass);

            @Override
            public SimpleBufferCodec<F, ?> codec() {
                return codec;
            }

            @Override
            @SuppressWarnings("unchecked")
            public F copy(F var1) {
                // todo: specialized codec? (initial boolean chcek)
                return codec instanceof IdentityBufferCodec<?> identityBufferCodec ? ((IdentityBufferCodec<F>)identityBufferCodec).encode(var1) : NullabilityUtils.copyIfNotNull(var1);
            }

            @Override
            public DataHolderType getHolderType() {
                return DataHolderType.HOLDER;
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

    static <F> Identity<F> identity(IdentityBufferCodec<F> codec) {
        return () -> codec;
    }

    static <F> Identity<F> identity(AbstractNMS nms, Class<F> toClass) {
        return () -> nms.getFirstNMSCodec(toClass);
    }

    static <F> Identity<F> identity(Class<F> toClass) {
        return identity(NMS, toClass);
    }

    interface Identity<F> extends EntityDataSerializer<F> {
        default F copy(F value) {
            return value;
        }

        @Override
        default DataHolderType getHolderType() {
            return DataHolderType.NORMAL;
        }
    }
}
