package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.Either;
import it.jakegblp.lusk.nms.core.bukkit.BukkitHelper;
import it.jakegblp.lusk.nms.core.event.client.EntityStatusPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.effect.InternalEntityEffect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@AllArgsConstructor
@NullMarked
public class EntityEventPacket implements ClientboundPacketWithEntityId<EntityStatusPacketEvent> {
    protected int entityId;
    // todo: properly implement, but keep method to get/set byte value
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

    public @Nullable Either<EntityEffect, InternalEntityEffect> getEffect() {
        return BukkitHelper.getEntityEffectById(this.data);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeByte(data);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        entityId = buffer.readInt();
        data = buffer.readByte();
    }

    @Override
    public EntityStatusPacketEvent createEvent(Player player, boolean async) {
        return new EntityStatusPacketEvent(this, player, async);
    }

    @Override
    public EntityEventPacket copy() {
        return new EntityEventPacket(entityId, data);
    }
}
