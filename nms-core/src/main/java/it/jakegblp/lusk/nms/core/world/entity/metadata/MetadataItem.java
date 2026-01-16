package it.jakegblp.lusk.nms.core.world.entity.metadata;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.util.BufferSerializable;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@NoArgsConstructor
@AllArgsConstructor
public class MetadataItem<E extends Entity, T> implements MetadataKeyReference<E, T>, BufferSerializable<MetadataItem<? extends Entity, @Nullable T>>, Copyable<MetadataItem<? extends Entity, T>> {
    private @Range(from = 0, to = 255) int id = -1;
    private @Nullable T value;
    private @NotNull Class<E> entityClass;
    private @NotNull EntityDataSerializer<T> serializer;
    private @NotNull Class<T> rawValueClass;

    public MetadataItem(
            @Range(from = 0, to = 255) int id,
            @Nullable T value,
            Class<E> entityClass,
            Class<T> valueClass) {
        this(id, value, entityClass, EntityDataSerializer.simple(valueClass), valueClass);
    }

    public MetadataItem(
            @Range(from = 0, to = 255) int id,
            @Nullable T value,
            EntityDataSerializer<T> serializer) {
        this(id, value, (Class<E>) Entity.class, serializer, serializer.codec().getFromClass());
    }

    public MetadataItem(
            @Range(from = 0, to = 255) int id,
            @Nullable T value,
            Class<T> valueClass) {
        this(id, value, (Class<E>) Entity.class, EntityDataSerializer.simple(valueClass), valueClass);
    }

    public MetadataItem(@Range(from = 0, to = 255) int id, SimpleByteBuf buffer) {
        read(buffer, id);
    }

    public MetadataItem(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        int serializedId = NMS.getNMSSerializerId(serializer);
        if (serializedId < 0)
            throw new EncoderException("Unknown serializer type " + this.serializer);
        else if (value == null)
            throw new EncoderException("Null value not allowed in this implementation");
        else {
            buffer.writeByte(id);
            buffer.writeVarInt(serializedId);
            serializer.codec().writeFrom(value, buffer);
        }
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        read(buffer, buffer.readByte());
    }

    public void read(SimpleByteBuf buffer, int id) {
        int serializerId = buffer.readVarInt();
        EntityDataSerializer<?> serializer = NMS.getSerializer(serializerId);
        if (serializer == null) {
            throw new DecoderException("Unknown serializer id " + serializerId);
        } else {
            read(buffer, id, (EntityDataSerializer<T>) serializer);
        }
    }
    public void read(SimpleByteBuf buffer, int id, EntityDataSerializer<T> serializer) {
        this.id = id;
        this.serializer = serializer;
        this.value = serializer.codec().readFrom(buffer);
    }

    @Override
    public @Range(from = 0, to = 255) int id() {
        return id;
    }

    @Override
    public @NotNull Class<E> entityClass() {
        return entityClass;
    }

    public @Nullable T value() {
        return value;
    }

    @Override
    public @NotNull EntityDataSerializer<T> serializer() {
        return serializer;
    }

    @Override
    public @NotNull Class<T> rawValueClass() {
        return rawValueClass;
    }

    @Override
    public MetadataItem<? extends Entity, @Nullable T> copy() {
        return new MetadataItem<>(id, NullabilityUtils.copyIfNotNull(value), entityClass, serializer, rawValueClass);
    }
}