package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import it.jakegblp.lusk.nms.core.world.entity.teams.TeamParameters;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TeamUpdatePacketEvent extends TeamPacketEvent {

    private static final HandlerList handlers = new HandlerList();

    protected TeamParameters parameters;

    public TeamUpdatePacketEvent(TeamPacket teamPacket, Player player, boolean async) {
        this(player, teamPacket.getName(), teamPacket.getParameters(), async);
    }

    public TeamUpdatePacketEvent(Player player, String name, TeamParameters parameters, boolean async) {
        super(player, name, async);
        this.parameters = parameters.copy();
    }

    public TeamParameters getParameters() {
        return parameters.copy();
    }

    public void setParameters(TeamParameters parameters) {
        markModified();
        this.parameters = parameters.copy();
    }

    @Override
    public TeamPacket createPacket() {
        return TeamPacket.updateInfo(name, parameters);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
