package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public class EntityMetadataPacketEvent extends ClientPacketEvent<EntityMetadataPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;
    protected Class<? extends Entity> target;
    protected EntityMetadata entityMetadata;
    protected World world;

    public EntityMetadataPacketEvent(EntityMetadataPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.target = packet.getTarget();
        this.entityMetadata = packet.getEntityMetadata();
        this.world = player.getWorld();
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    public void setTarget(Class<? extends Entity> target) {
        markModified();
        this.target = target;
    }

    public void setEntityMetadata(EntityMetadata entityMetadata) {
        markModified();
        this.entityMetadata = entityMetadata;
    }

    @Override
    public boolean isModified() {
        return super.isModified() || getEntityMetadata().isModified();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public EntityMetadataPacket createPacket() {
        return new EntityMetadataPacket(getEntityId(), getTarget(), getEntityMetadata());
    }
}
