package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeyReference;
import lombok.*;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Entity_Metadata">Entity Metadata Packet</a>
 */
@Getter
@Setter
@NullMarked
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public final class EntityMetadataPacket implements ClientboundPacketWithId {
    private int id;
    private Class<? extends Entity> target;
    private EntityMetadata entityMetadata;

    public EntityMetadataPacket(
            int id,
            Class<? extends Entity> target,
            Map<? extends MetadataKeyReference<? extends Entity, ?>, ?> metadata
    ) {
        this(id, target, EntityMetadata.of(metadata));
    }

    public EntityMetadataPacket(
            int id,
            Map<? extends MetadataKeyReference<? extends Entity, ?>, ?> metadata
    ) {
        this(id, Entity.class, metadata);
    }

    public EntityMetadataPacket(
            int id,
            EntityMetadata metadata
    ) {
        this(id, Entity.class, metadata);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        NMS.write(this, buffer);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        var read = NMS.read(getClass(), buffer);
        id = read.id;
        target = read.target;
        entityMetadata = read.entityMetadata;
    }

    @Override
    public EntityMetadataPacket copy() {
        return new EntityMetadataPacket(id, target, entityMetadata.copy());
    }
}