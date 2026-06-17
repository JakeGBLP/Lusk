package it.jakegblp.lusk.nms.core.event.server;

import it.jakegblp.lusk.nms.core.event.PacketWrapperEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import org.bukkit.entity.Player;

public abstract class ServerPacketEvent<P extends ServerboundPacket> extends PacketWrapperEvent<P> {
    protected ServerPacketEvent(Player player, boolean async) {
        super(player, async);
    }
}
