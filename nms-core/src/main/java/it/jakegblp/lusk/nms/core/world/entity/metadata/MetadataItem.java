package it.jakegblp.lusk.nms.core.world.entity.metadata;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.serialization.BufferSerializable;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@ToString
public class MetadataItem<E extends Entity, T> implements MetadataKeyReference<E, T>, BufferSerializable<MetadataItem<? extends Entity, @Nullable T>>, Copyable<MetadataItem<? extends Entity, T>> {
    private @Range(from = 0, to = 255) int id = -1;
    private @Nullable T value;
    private @NotNull Class<E> entityClass;
    private @NotNull EntityDataSerializer<T> serializer;

    public MetadataItem(
            @Range(from = 0, to = 255) int id,
            @Nullable T value,
            @NotNull Class<E> entityClass,
            @NotNull EntityDataSerializer<T> serializer) {
        this.id = id;
        this.value = value;
        this.entityClass = entityClass;
        this.serializer = serializer;
    }

    public MetadataItem(
            @Range(from = 0, to = 255) int id,
            @Nullable T value,
            EntityDataSerializer<T> serializer) {
        this(id, value, (Class<E>) Entity.class, serializer);
    }

    public MetadataItem(
            @Range(from = 0, to = 255) int id,
            @Nullable T value,
            Class<T> valueClass) {
        this(id, value, (Class<E>) Entity.class, EntityDataSerializer.simple(valueClass));
    }

    public MetadataItem(@Range(from = 0, to = 255) int id, SimpleByteBuf buffer) {
        read(buffer, id);
    }

    public MetadataItem(SimpleByteBuf buffer) {
        read(buffer);
    }
    public MetadataItem(SimpleByteBuf buffer, int id) {
        read(buffer, id);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        int serializedId = NMS.getSerializerId(serializer);
        if (serializedId < 0)
            throw new EncoderException("Unknown serializer type " + this.serializer);
        else if (value == null && !serializer.codec().key().isOptional())
            throw new EncoderException("Null value not allowed in this implementation");
        else {
            buffer.writeByte(id);
            buffer.writeVarInt(serializedId);
            serializer.codec().write(buffer, value);
        }
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        read(buffer, buffer.readByte());
    }

    public void read(SimpleByteBuf buffer, int id) {
        int serializerId = buffer.readVarInt();
        var serializer = NMS.getSerializer(serializerId);
        if (serializer == null) {
            throw new DecoderException("Unknown serializer id " + serializerId);
        } else {
            read(buffer, id, (EntityDataSerializer<T>) serializer);
        }
    }

    public void read(SimpleByteBuf buffer, int id, EntityDataSerializer<T> serializer) {
        this.id = id;
        this.serializer = serializer;
        this.value = serializer.codec().read(buffer);
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
    public MetadataItem<? extends Entity, @Nullable T> copy() {
        return new MetadataItem<>(id, NullabilityUtils.copyIfNotNull(value), entityClass, serializer);
    }
}