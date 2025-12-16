package it.jakegblp.lusk.nms.core.events;

import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import org.bukkit.entity.Player;

public abstract class ServerPacketEvent<P extends ServerboundPacket> extends PacketEvent<P> {

    protected ServerPacketEvent(P packet, Player player) {
        super(packet, player);
    }

    protected ServerPacketEvent(Object nmsPacket, Player player) {
        super(nmsPacket, player);
    }
}
