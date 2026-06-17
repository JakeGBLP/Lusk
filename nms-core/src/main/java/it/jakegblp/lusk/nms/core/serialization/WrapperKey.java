package it.jakegblp.lusk.nms.core.serialization;

public interface WrapperKey<T> {
    TypeKey<T> inner();
}
