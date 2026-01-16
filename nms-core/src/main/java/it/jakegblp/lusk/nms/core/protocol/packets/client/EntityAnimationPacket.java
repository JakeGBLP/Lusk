package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.EntityAnimation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EntityAnimationPacket implements ClientboundPacketWithId {

    private int id;
    private EntityAnimation entityAnimation;

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
        buffer.writeVarInt(entityAnimation.getActionId());
        buffer.writeUnsignedByte(entityAnimation.getActionId());
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        id = buffer.readVarInt();
        entityAnimation = EntityAnimation.fromId(buffer.readUnsignedByte());
    }

    @Override
    public EntityAnimationPacket copy() {
        return new EntityAnimationPacket(id, entityAnimation);
    }
}
