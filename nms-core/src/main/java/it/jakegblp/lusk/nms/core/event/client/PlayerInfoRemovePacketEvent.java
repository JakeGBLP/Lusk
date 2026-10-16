package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoRemovePacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NullMarked
@Getter
public class PlayerInfoRemovePacketEvent extends ClientPacketEvent<PlayerInfoRemovePacket> {

    private static final HandlerList handlers = new HandlerList();

    protected List<UUID> players;

    public PlayerInfoRemovePacketEvent(PlayerInfoRemovePacket packet, Player player, boolean async) {
        super(player, async);
        this.players = new ArrayList<>(packet.getPlayers());
    }

    public void setEntityIds(List<UUID> players) {
        this.players = new ArrayList<>(players);
    }

    @Contract("-> new")
    public List<UUID> getPlayers() {
        return new ArrayList<>(players);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public PlayerInfoRemovePacket createPacket() {
        return new PlayerInfoRemovePacket(getPlayers());
    }
}
