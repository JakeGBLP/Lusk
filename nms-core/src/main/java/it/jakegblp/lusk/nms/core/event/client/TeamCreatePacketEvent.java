package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import it.jakegblp.lusk.nms.core.world.entity.teams.TeamParameters;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

@NullMarked
public class TeamCreatePacketEvent extends TeamPacketEvent {

    private static final HandlerList handlers = new HandlerList();

    protected TeamParameters parameters;
    protected Set<String> initialMembers;

    public TeamCreatePacketEvent(TeamPacket teamPacket, Player player, boolean async) {
        this(player, teamPacket.getName(), teamPacket.getParameters(), teamPacket.getMembers(), async);
    }

    public TeamCreatePacketEvent(Player player, String name, TeamParameters parameters, Set<String> initialMembers, boolean async) {
        super(player, name, async);
        this.parameters = parameters.copy();
        this.initialMembers = new HashSet<>(initialMembers);
    }

    public Set<String> getInitialMembers() {
        return new HashSet<>(this.initialMembers);
    }

    public void setInitialMembers(Set<String> initialMembers) {
        markModified();
        this.initialMembers = new HashSet<>(initialMembers);
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
        return TeamPacket.create(name, parameters, initialMembers);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
