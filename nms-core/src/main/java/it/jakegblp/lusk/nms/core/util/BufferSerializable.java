package it.jakegblp.lusk.nms.core.util;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public interface BufferSerializable<T extends BufferSerializable<T>> {
    void write(SimpleByteBuf buffer);
    void read(SimpleByteBuf buffer);

    @SuppressWarnings("unchecked")
    default SimpleBufferCodec<?, T> getCodec() {
        return (SimpleBufferCodec<?, T>) NMS.getFirstCodec(getClass());
    }

}
