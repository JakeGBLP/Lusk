package it.jakegblp.lusk.nms.core.events;

import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PacketReceiveEvent<P extends ServerboundPacket> extends PacketEvent<P> implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    protected boolean cancelled;

    public PacketReceiveEvent(P packet) {
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
