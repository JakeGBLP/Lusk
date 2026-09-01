package it.jakegblp.lusk.nms.core.serialization.key;

public interface WrapperKey<T> {
    TypeKey<T> inner();
}
