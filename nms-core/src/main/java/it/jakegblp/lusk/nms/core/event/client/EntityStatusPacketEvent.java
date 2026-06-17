package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityEventPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public class EntityStatusPacketEvent extends ClientPacketEvent<EntityEventPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;
    protected byte data;

    public EntityStatusPacketEvent(EntityEventPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.data = packet.getData();
    }

    public void setData(byte data) {
        markModified();
        this.data = data;
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public EntityEventPacket createPacket() {
        return new EntityEventPacket(getEntityId(), getData());
    }
}
