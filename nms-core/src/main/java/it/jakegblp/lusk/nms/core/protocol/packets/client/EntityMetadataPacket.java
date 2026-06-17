package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.EntityMetadataPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeyReference;
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

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Entity_Metadata">Entity Metadata Packet</a>
 */
@Getter
@Setter
@NullMarked
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public final class EntityMetadataPacket implements ClientboundPacketWithEntityId<EntityMetadataPacketEvent> {
    private int entityId;
    private Class<? extends Entity> target;
    private EntityMetadata entityMetadata;

    public EntityMetadataPacket(SimpleByteBuf buf) {
        entityId = buf.readVarInt();
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
            Map<? extends MetadataKeyReference<? extends Entity, ?>, ?> metadata
    ) {
        this(entityId, target, EntityMetadata.of(metadata));
    }

    public EntityMetadataPacket(
            int entityId,
            Map<? extends MetadataKeyReference<? extends Entity, ?>, ?> metadata
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

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(entityId);

        for (MetadataItem<? extends Entity, ?> item : entityMetadata.items()) {
            if (item == null) continue;
            item.write(buffer);
        }

        buffer.writeByte(255);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        List<MetadataItem<? extends Entity, ?>> result = new ArrayList<>();

        int id;
        while((id = buffer.readUnsignedByte()) != 255) {
            result.add(new MetadataItem<>(buffer, id));
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