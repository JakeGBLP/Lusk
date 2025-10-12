package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Entity;

import java.util.List;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Entity_Metadata">Entity Metadata Packet</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
public final class EntityMetadataPacket implements ClientboundPacketWithId {
    private int entityId;
    private Class<? extends Entity> target;
    private EntityMetadata entityMetadata;

    public EntityMetadataPacket(
            int id,
            Class<? extends Entity> target,
            List<MetadataItem<? extends Entity, ?>> items
    ) {
        this(id, target, new EntityMetadata(items));
    }

    public EntityMetadataPacket(
            int id,
            List<MetadataItem<? extends Entity, ?>> items
    ) {
        this(id, Entity.class, items);
    }

    public EntityMetadataPacket(
            int id,
            EntityMetadata metadata
    ) {
        this(id, Entity.class, metadata);
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSEntityMetadataPacket(this);
    }
}