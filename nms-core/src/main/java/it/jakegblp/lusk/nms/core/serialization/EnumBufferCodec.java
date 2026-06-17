package it.jakegblp.lusk.nms.core.serialization;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record EnumBufferCodec<T extends Enum<T>>(SimpleKey<T> key) implements BufferCodec<T> {

    public EnumBufferCodec(Class<T> enumClass, int variant) {
        this(new SimpleKey<>(enumClass, variant));
    }

    public EnumBufferCodec(Class<T> enumClass) {
        this(enumClass, 0);
    }

    @Override
    public void write(SimpleByteBuf buf, T value) {
        buf.writeEnum(value);
    }

    @Override
    public T read(SimpleByteBuf buf) {
        return buf.readEnum(type());
    }

    @Override
    public EnumBufferCodec<T> withVariant(int variant) {
        return new EnumBufferCodec<>(key.type(), variant);
    }
}
