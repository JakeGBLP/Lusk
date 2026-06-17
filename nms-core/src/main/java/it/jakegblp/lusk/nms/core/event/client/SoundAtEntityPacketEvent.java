package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.SoundEntityPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class SoundAtEntityPacketEvent extends SoundPacketEvent<SoundEntityPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;

    public SoundAtEntityPacketEvent(SoundEntityPacket packet, Player player, boolean async) {
        super(player, packet.getSound(), packet.getSoundCategory(), packet.getVolume(), packet.getPitch(), packet.getSeed(), async);
        this.entityId = player.getEntityId();
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    @Override
    public @NotNull SoundEntityPacket createPacket() {
        return new SoundEntityPacket(getSound(), getSoundCategory(), getEntityId(), getVolume(), getPitch(), getSeed());
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
