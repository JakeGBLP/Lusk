package it.jakegblp.lusk.nms.core.event;

import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public abstract class PacketWrapperEvent<P extends Packet> extends ProtocolEvent {

    protected boolean modified;

    protected PacketWrapperEvent(Player player, boolean async) {
        super(player, async);
    }

    protected void markModified() {
        this.modified = true;
    }

    @Contract("-> new")
    public abstract P createPacket();

}
