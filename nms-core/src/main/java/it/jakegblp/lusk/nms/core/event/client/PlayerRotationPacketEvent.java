package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
@Availability(addedIn = "1.21.2")
public class PlayerRotationPacketEvent extends ClientPacketEvent<PlayerRotationPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected float yaw, pitch;

    @Availability(addedIn = "1.21.2")
    public PlayerRotationPacketEvent(PlayerRotationPacket packet, Player player, boolean async) {
        super(player, async);
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
    }

    public void setPitch(float pitch) {
        markModified();
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        markModified();
        this.yaw = yaw;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    @Availability(addedIn = "1.21.2")
    public PlayerRotationPacket createPacket() {
        return new PlayerRotationPacket(getYaw(), getPitch());
    }
}
