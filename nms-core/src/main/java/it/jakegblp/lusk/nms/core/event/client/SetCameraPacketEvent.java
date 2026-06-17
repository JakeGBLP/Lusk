package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.SetCameraPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public class SetCameraPacketEvent extends ClientPacketEvent<SetCameraPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int cameraId;

    public SetCameraPacketEvent(SetCameraPacket packet, Player player, boolean async) {
        super(player, async);
        this.cameraId = packet.getCameraId();
    }

    public void setCameraId(int cameraId) {
        markModified();
        this.cameraId = cameraId;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public SetCameraPacket createPacket() {
        return new SetCameraPacket(getCameraId());
    }
}
