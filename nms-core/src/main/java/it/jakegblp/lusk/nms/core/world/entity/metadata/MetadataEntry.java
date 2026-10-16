package it.jakegblp.lusk.nms.core.world.entity.metadata;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.serialization.BufferSerializable;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@ToString
@NullMarked
@Getter
public final class MetadataEntry<BaseEntity extends Entity, Type> implements BufferSerializable<MetadataEntry<BaseEntity, Type>>, Copyable<MetadataEntry<BaseEntity, Type>> {

    @SuppressWarnings("NotNullFieldNotInitialized")
    private EntityDataKey<BaseEntity, Type, ?, ?, ?> key;
    private @Nullable Type value;

    public MetadataEntry(EntityDataKey<BaseEntity, Type, ?, ?, ?> key, @Nullable Type value) {
        this.key = key;
        this.value = value;
    }

    public MetadataEntry(@Range(from = 0, to = 255) int id, SimpleByteBuf buffer) {
        read(buffer, id);
    }

    public MetadataEntry(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(SimpleByteBuf buffer) {
        EntityDataSerializer<Object, ?> serializer = (EntityDataSerializer<Object, ?>) key.getSerializer();
        int serializedId = NMS.getSerializerId(serializer);
        if (serializedId < 0)
            throw new EncoderException("Unknown serializer type " + serializer);
        else if (value == null && !serializer.codec().key().isOptional())
            throw new EncoderException("Null value not allowed in this implementation");
        else {
            buffer.writeByte(key.getId());
            buffer.writeVarInt(serializedId);
            serializer.codec().write(buffer, key.asInternal(value));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public Object getSerializableValue() {
        return value == null ? null : key.asInternal(value);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        read(buffer, buffer.readByte());
    }

    public void read(SimpleByteBuf buffer, int id) {
        int serializerId = buffer.readVarInt();
        var serializer = NMS.getSerializer(serializerId);
        if (serializer == null)
            throw new DecoderException("Unknown serializer id " + serializerId);
        else
            read(buffer, id, serializer);
    }

    @SuppressWarnings("unchecked")
    public void read(SimpleByteBuf buffer, int id, EntityDataSerializer<?, ?> serializer) {
        for (var entry : NMS.getMetadataKeyRegistry().metadataEntries().entries()) {
            var metadataKey = entry.getValue();
            //System.out.println("this: " + id + " " + serializer.key());
            //System.out.println("loop: " + metadataKey.getId() + " " + metadataKey.getSerializer().key() + ", name: " + metadataKey.getName() + ", entity classes: " + entry.getValue().getEntityClasses());
            if (metadataKey.getId() == id && metadataKey.getSerializer().equals(serializer)) {
                this.key = (EntityDataKey<BaseEntity, Type, ?, ?, ?>) metadataKey;
                var readValue = serializer.codec().read(buffer);
                this.value = key.canBeSetTo(readValue) ? (Type) readValue : (Type) ((EntityDataKey) key).asPretty(readValue);
                return;
            }
        }
        throw new IllegalStateException("All metadata keys were scanned but a matching one was not found. Id: "+id+", Serializer: "+serializer);
    }

    @Override
    public MetadataEntry<BaseEntity, Type> copy() {
        return new MetadataEntry<>(key, NullabilityUtils.copyIfNotNull(value));
    }

}