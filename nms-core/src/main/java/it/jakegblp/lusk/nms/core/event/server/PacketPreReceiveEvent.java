package it.jakegblp.lusk.nms.core.event.server;

import it.jakegblp.lusk.nms.core.event.ProtocolEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@NullMarked
public class PacketPreReceiveEvent extends ProtocolEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PacketPreReceiveEvent(Player player, boolean async) {
        super(player, async);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
