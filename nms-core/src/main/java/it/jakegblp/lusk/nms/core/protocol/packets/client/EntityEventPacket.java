package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.Either;
import it.jakegblp.lusk.nms.core.bukkit.BukkitHelper;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.effect.InternalEntityEffect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.EntityEffect;

@Getter
@Setter
@AllArgsConstructor
public class EntityEventPacket implements ClientboundPacketWithId {
    protected int id;
    protected byte data;

    public EntityEventPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @SuppressWarnings("UnstableApiUsage")
    public EntityEventPacket(int entityID, EntityEffect entityEffect) {
        this(entityID, entityEffect.getData());
    }

    public EntityEventPacket(int entityID, InternalEntityEffect internalEntityEffect) {
        this(entityID, internalEntityEffect.getData());
    }

    @SuppressWarnings("UnstableApiUsage")
    public void setEffect(EntityEffect entityEffect) {
        this.data = entityEffect.getData();
    }

    public void setEffect(InternalEntityEffect internalEntityEffect) {
        this.data = internalEntityEffect.getData();
    }

    public void setEffect(byte data) {
        this.data = data;
    }

    public byte getEventId() {
        return this.data;
    }

    public Either<EntityEffect, InternalEntityEffect> getEffect() {
        return BukkitHelper.getEntityEffectById(this.data);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeInt(id);
        buffer.writeByte(data);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        id = buffer.readInt();
        data = buffer.readByte();
    }

    @Override
    public EntityEventPacket copy() {
        return new EntityEventPacket(id, data);
    }
}
