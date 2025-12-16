package it.jakegblp.lusk.nms.core.events;

import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public abstract class PacketEvent<P extends Packet> extends PlayerEvent {

    @Nullable
    private Supplier<P> supplier;
    protected P packet;
    @Getter
    protected boolean resolved;
    @Getter
    protected boolean modified = false;

    protected PacketEvent(P packet, Player player) {
        super(player);
        this.packet = packet;
        this.resolved = true;
    }

    @SuppressWarnings("unchecked")
    protected PacketEvent(Object nmsPacket, Player player) {
        super(player);
        this.supplier = () -> (P) NMS.fromNMSPacket(nmsPacket);
        this.resolved = false;
    }

    public P getPacket() {
        if (!resolved && supplier != null)
            setPacket(supplier.get());
        return packet;
    }

    public void setPacket(P packet) {
        this.packet = packet;
        if (resolved) modified = true;
        this.supplier = null;
        this.resolved = true;
    }
}
