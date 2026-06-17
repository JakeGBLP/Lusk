package it.jakegblp.lusk.nms.core.event;

import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@NullMarked
public abstract class PacketExchangeEvent<P extends Packet> extends ProtocolEvent {

    protected P packet;

    protected PacketExchangeEvent(P packet, Player player, boolean async) {
        super(player, async);
        this.packet = packet;
    }

}