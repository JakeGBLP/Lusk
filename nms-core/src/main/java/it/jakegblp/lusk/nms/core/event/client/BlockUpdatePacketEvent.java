package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.BlockUpdatePacket;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BlockUpdatePacketEvent extends ClientPacketEvent<BlockUpdatePacket> {

    private static final HandlerList handlers = new HandlerList();

    protected Vector position;
    protected BlockData blockData;
    protected World world;

    public BlockUpdatePacketEvent(BlockUpdatePacket packet, Player player, boolean async) {
        super(player, async);
        this.position = packet.getPosition().clone();
        this.blockData = packet.getBlockData().clone();
        this.world = player.getWorld();
    }

    @Contract("-> new")
    public BlockData getBlockData() {
        return blockData.clone();
    }

    @Contract("-> new")
    public Vector getPosition() {
        return position.clone();
    }

    public void setPosition(Vector position) {
        this.position = position.clone();
        markModified();
    }

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
        markModified();
    }

    @Contract("-> new")
    public Location getLocation() {
        return getPosition().toLocation(world);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public BlockUpdatePacket createPacket() {
        return new BlockUpdatePacket(position.toBlockVector(), blockData.clone());
    }
}
