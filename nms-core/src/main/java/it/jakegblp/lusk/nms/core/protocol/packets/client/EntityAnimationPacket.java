package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.world.entity.EntityAnimation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
@AllArgsConstructor
public class EntityAnimationPacket implements ClientboundPacketWithId {

    private int entityId;
    private EntityAnimation entityAnimation;

    public EntityAnimationPacket(int entityId, int entityAnimationId) {
        this(entityId, EntityAnimation.fromId(entityAnimationId));
    }

    public int getEntityAnimationId() {
        return entityAnimation.getActionId();
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSEntityAnimationPacket(this);
    }
}
