package it.jakegblp.lusk.nms.core.serialization.key;

public record SimpleKey<T>(Class<T> type, int variant) implements TypeKey<T> {

    @Override
    public Class<?> rawType() {
        return type;
    }
}