package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import org.bukkit.entity.Player;

public interface ClientboundPacket extends Packet {

    default void send(Player player) {
        NMSApi.sendPacket(player, this);
    }

}
