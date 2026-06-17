package it.jakegblp.lusk.nms.core.serialization;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public interface BufferSerializable<T extends BufferSerializable<T>> {
    void write(SimpleByteBuf buffer);
    void read(SimpleByteBuf buffer);

    @SuppressWarnings("unchecked")
    default BufferCodec<T> getCodec() {
        return (BufferCodec<T>) NMS.getCodecs().get(getClass());
    }

}
