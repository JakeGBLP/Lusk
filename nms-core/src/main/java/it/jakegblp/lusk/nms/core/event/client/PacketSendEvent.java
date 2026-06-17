package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.event.PacketExchangeEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PacketSendEvent<P extends ClientboundPacket<?>> extends PacketExchangeEvent<P> {

    private static final HandlerList HANDLERS = new HandlerList();

    public PacketSendEvent(P packet, Player player, boolean async) {
        super(packet, player, async);
    }

    @Override
    public @NotNull P getPacket() {
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
