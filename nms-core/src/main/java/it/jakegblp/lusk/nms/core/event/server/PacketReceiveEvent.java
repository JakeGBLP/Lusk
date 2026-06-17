package it.jakegblp.lusk.nms.core.event.server;

import it.jakegblp.lusk.nms.core.event.PacketExchangeEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@NullMarked
public class PacketReceiveEvent<P extends ServerboundPacket> extends PacketExchangeEvent<P> {

    private static final HandlerList HANDLERS = new HandlerList();

    public PacketReceiveEvent(P packet, Player player, boolean async) {
        super(packet, player, async);
    }

    @Override
    public P getPacket() {
        return super.getPacket();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
