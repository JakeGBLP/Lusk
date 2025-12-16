package it.jakegblp.lusk.nms.core.events;

import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import org.bukkit.entity.Player;

public abstract class ClientPacketEvent<P extends ClientboundPacket> extends PacketEvent<P> {

    protected ClientPacketEvent(P packet, Player player) {
        super(packet, player);
    }

    protected ClientPacketEvent(Object nmsPacket, Player player) {
        super(nmsPacket, player);
    }
}
