package it.jakegblp.lusk.nms.core.util;

public interface LazyProducer {

    void markDirty();

    boolean isDirty();

    void produce();
}
