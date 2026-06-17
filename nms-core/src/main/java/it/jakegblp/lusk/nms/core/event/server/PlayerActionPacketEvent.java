package it.jakegblp.lusk.nms.core.event.server;

import it.jakegblp.lusk.nms.core.protocol.packets.server.PlayerActionPacket;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@Getter
@NullMarked
public class PlayerActionPacketEvent extends ServerPacketEvent<PlayerActionPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected Vector position;
    protected BlockFace blockFace;
    protected PlayerActionPacket.Action action;
    protected int sequence;

    public PlayerActionPacketEvent(PlayerActionPacket packet, Player player, boolean async) {
        super(player, async);
        this.position = packet.getPosition().clone();
        this.blockFace = packet.getBlockFace();
        this.action = packet.getAction();
        this.sequence = packet.getSequence();
    }

    @Contract("-> new")
    public Location getLocation() {
        return position.toLocation(player.getWorld());
    }

    public void setPosition(Vector position) {
        markModified();
        this.position = position;
    }

    public void setAction(PlayerActionPacket.Action action) {
        markModified();
        this.action = action;
    }

    public void setBlockFace(BlockFace blockFace) {
        markModified();
        this.blockFace = blockFace;
    }

    public void setSequence(int sequence) {
        markModified();
        this.sequence = sequence;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public PlayerActionPacket createPacket() {
        return new PlayerActionPacket(position.toBlockVector(), blockFace, action, sequence);
    }
}
