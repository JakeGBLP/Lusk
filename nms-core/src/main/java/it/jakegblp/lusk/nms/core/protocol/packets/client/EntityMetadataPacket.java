package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.EntityMetadataPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataEntry;
import it.jakegblp.lusk.nms.core.world.entity.metadata.news.ComponentEntityDataKey;
import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Entity_Metadata">Entity Metadata Packet</a>
 */
@Getter
@Setter
@NullMarked
@ToString
@EqualsAndHashCode(callSuper = false)
public final class EntityMetadataPacket implements ClientboundPacketWithEntityId<EntityMetadataPacketEvent> {
    private int entityId;
    private Class<? extends Entity> target;
    @SuppressWarnings("NotNullFieldNotInitialized")
    private EntityMetadata entityMetadata;

    public EntityMetadataPacket(SimpleByteBuf buf) {
        read(buf);
    }

    public EntityMetadataPacket(
            int entityId,
            Class<? extends Entity> target,
            EntityMetadata entityMetadata
    ) {
        this.entityId = entityId;
        this.target = target;
        this.entityMetadata = entityMetadata.copy();
    }

    public EntityMetadataPacket(
            int entityId,
            Class<? extends Entity> target,
            Map<? extends EntityDataKey<? extends Entity, ?, ?, ?, ?>, ?> metadata
    ) {
        this(entityId, target, EntityMetadata.of(metadata));
    }

    public EntityMetadataPacket(
            int entityId,
            Map<? extends EntityDataKey<? extends Entity, ?, ?, ?, ?>, ?> metadata
    ) {
        this(entityId, Entity.class, metadata);
    }

    public EntityMetadataPacket(
            int entityId,
            EntityMetadata metadata
    ) {
        this(entityId, Entity.class, metadata);
    }

    public void setEntityMetadata(EntityMetadata entityMetadata) {
        this.entityMetadata = entityMetadata.copy();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(entityId);

        for (MetadataEntry item : entityMetadata.items()) {
            if (item == null) continue;
            var key = item.getKey();
            buffer.writeByte(key.getId());
            var serializer = key.getSerializer();
            buffer.writeVarInt(NMS.getSerializerId(serializer));
            //NMS.write(buffer, item.value(), item.codecKey());
            Object value;
            if (key instanceof ComponentEntityDataKey componentEntityDataKey) {
                assert item.getValue() != null;
                Object prettyValue = componentEntityDataKey.createParentObject(item.getValue());
                value = prettyValue instanceof NMSObject nmsObject ? nmsObject.asNMS() : NMS.toNMS(prettyValue);
            } else
                value = item.getSerializableValue();
            serializer.codec().write(buffer, value);
        }

        buffer.writeByte(255);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.entityId = buffer.readVarInt();
        List<MetadataEntry<? extends Entity, ?>> result = new ArrayList<>();

        short index;
        while((index = buffer.readUnsignedByte()) != 255) {
            result.add(new MetadataEntry<>(index, buffer));
        }

        entityMetadata = EntityMetadata.of(result);
    }

    @Override
    public EntityMetadataPacketEvent createEvent(Player player, boolean async) {
        return new EntityMetadataPacketEvent(this, player, async);
    }

    @Override
    public EntityMetadataPacket copy() {
        return new EntityMetadataPacket(entityId, target, entityMetadata.copy());
    }
}