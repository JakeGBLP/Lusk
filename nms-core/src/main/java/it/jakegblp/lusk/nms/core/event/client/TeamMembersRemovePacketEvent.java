package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

@NullMarked
public class TeamMembersRemovePacketEvent extends TeamPacketEvent {

    private static final HandlerList handlers = new HandlerList();

    protected Set<String> members;

    public TeamMembersRemovePacketEvent(TeamPacket teamPacket, Player player, boolean async) {
        this(player, teamPacket.getName(), teamPacket.getMembers(), async);
    }

    public TeamMembersRemovePacketEvent(Player player, String name, Set<String> members, boolean async) {
        super(player, name, async);
        this.members = new HashSet<>(members);
    }

    public Set<String> getMembers() {
        return new HashSet<>(this.members);
    }

    public void setMembers(Set<String> members) {
        markModified();
        this.members = new HashSet<>(members);
    }

    @Override
    public TeamPacket createPacket() {
        return TeamPacket.remove(name, members);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
