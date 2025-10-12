package it.jakegblp.lusk.nms.core.util;

@FunctionalInterface
public interface NMSObject<T> {
    T asNMS();
}