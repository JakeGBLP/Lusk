package it.jakegblp.lusk.nms.core.events;

import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PacketSendEvent<P extends ClientboundPacket> extends PacketEvent<P> implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    protected boolean cancelled;

    public PacketSendEvent(P packet) {
        super(packet);
    }

    @Override
    public P getPacket() {
        return super.getPacket();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
