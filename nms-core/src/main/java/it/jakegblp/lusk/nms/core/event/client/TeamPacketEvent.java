package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class TeamPacketEvent extends ClientPacketEvent<TeamPacket> {

    @Getter
    protected String name;

    public TeamPacketEvent(Player player, String name, boolean async) {
        super(player, async);
        this.name = name;
    }

    public void setName(String name) {
        markModified();
        this.name = name;
    }

}
