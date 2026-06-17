package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.event.PacketWrapperEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import org.bukkit.entity.Player;

public abstract class ClientPacketEvent<P extends ClientboundPacket<?>> extends PacketWrapperEvent<P> {

    // store world here?

    protected ClientPacketEvent(Player player, boolean async) {
        super(player, async);
    }
}
