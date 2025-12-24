package it.jakegblp.lusk.nms.core.util;

public interface TypedBufferCodec<From, To> extends BufferCodec<From, To> {
    Class<From> getFromClass();
    Class<To> getToClass();
}
