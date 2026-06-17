package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityAnimationPacket;
import it.jakegblp.lusk.nms.core.world.entity.EntityAnimation;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public class EntityAnimationPacketEvent extends ClientPacketEvent<EntityAnimationPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;
    protected EntityAnimation entityAnimation;

    public EntityAnimationPacketEvent(EntityAnimationPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.entityAnimation = packet.getEntityAnimation();
    }

    public void setEntityAnimation(EntityAnimation entityAnimation) {
        markModified();
        this.entityAnimation = entityAnimation;
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
    public EntityAnimationPacket createPacket() {
        return new EntityAnimationPacket(getEntityId(), getEntityAnimation());
    }
}
