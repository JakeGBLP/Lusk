package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TeamDeletePacketEvent extends TeamPacketEvent {

    private static final HandlerList handlers = new HandlerList();

    public TeamDeletePacketEvent(TeamPacket teamPacket, Player player, boolean async) {
        this(player, teamPacket.getName(), async);
    }

    public TeamDeletePacketEvent(Player player, String name, boolean async) {
        super(player, name, async);
    }

    @Override
    public TeamPacket createPacket() {
        return TeamPacket.delete(name);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
