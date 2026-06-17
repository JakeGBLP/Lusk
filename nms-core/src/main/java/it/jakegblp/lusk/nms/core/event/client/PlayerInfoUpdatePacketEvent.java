package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.PseudoEnumSet;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoUpdatePacket;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class PlayerInfoUpdatePacketEvent extends ClientPacketEvent<PlayerInfoUpdatePacket> {

    private static final HandlerList handlers = new HandlerList();

    protected List<PlayerInfo> playerInfos;
    protected PseudoEnumSet<PlayerInfoUpdatePacket.Action> actions;

    public PlayerInfoUpdatePacketEvent(PlayerInfoUpdatePacket packet, Player player, boolean async) {
        super(player, async);
        this.playerInfos = CommonUtils.copyList(packet.getPlayerInfos());
        this.actions = PseudoEnumSet.copyOf(packet.getActions());
    }

    @Contract("-> new")
    public List<PlayerInfo> getPlayerInfos() {
        return CommonUtils.copyList(playerInfos);
    }

    @Contract("-> new")
    public PseudoEnumSet<PlayerInfoUpdatePacket.Action> getActions() {
        return PseudoEnumSet.copyOf(actions);
    }

    public void setPlayerInfos(List<PlayerInfo> playerInfos) {
        this.playerInfos = CommonUtils.copyList(playerInfos);
    }

    public void setActions(PseudoEnumSet<PlayerInfoUpdatePacket.Action> actions) {
        this.actions = PseudoEnumSet.copyOf(actions);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public PlayerInfoUpdatePacket createPacket() {
        return new PlayerInfoUpdatePacket(getActions(), getPlayerInfos());
    }
}
