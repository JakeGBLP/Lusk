package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.EntityAnimationPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.EntityAnimation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
@AllArgsConstructor
public class EntityAnimationPacket implements ClientboundPacketWithEntityId<EntityAnimationPacketEvent> {

    protected int entityId;
    protected EntityAnimation entityAnimation;

    public EntityAnimationPacket(int entityId, int entityAnimationId) {
        this(entityId, EntityAnimation.fromId(entityAnimationId));
    }

    public EntityAnimationPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public int getEntityAnimationId() {
        return entityAnimation.getActionId();
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeUnsignedByte(getEntityAnimationId());
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        entityId = buffer.readVarInt();
        entityAnimation = EntityAnimation.fromId(buffer.readUnsignedByte());
    }

    @Override
    public EntityAnimationPacketEvent createEvent(Player player, boolean async) {
        return new EntityAnimationPacketEvent(this, player, async);
    }

    @Override
    public EntityAnimationPacket copy() {
        return new EntityAnimationPacket(entityId, entityAnimation);
    }
}
