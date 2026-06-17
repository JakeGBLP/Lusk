package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.BlockDestructionPacket;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public class BlockDestructionPacketEvent extends ClientPacketEvent<BlockDestructionPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;
    protected BlockVector position;
    protected @Range(from = -1, to = 9) byte blockDestructionStage;
    protected World world;

    public BlockDestructionPacketEvent(BlockDestructionPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.position = packet.getPosition().clone();
        this.blockDestructionStage = packet.getBlockDestructionStage();
        this.world = player.getWorld();
    }

    @Contract("-> new")
    public BlockVector getPosition() {
        return position.clone();
    }

    public void setPosition(BlockVector position) {
        markModified();
        this.position = position.clone();
    }

    public void setBlockDestructionStage(byte blockDestructionStage) {
        markModified();
        this.blockDestructionStage = blockDestructionStage;
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    @Contract("-> new")
    public Location getLocation() {
        return position.toLocation(world);
    }

    public void setLocation(Location location) {
        markModified();
        setPosition(location.toVector().toBlockVector());
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public BlockDestructionPacket createPacket() {
        return new BlockDestructionPacket(getEntityId(), getPosition(), getBlockDestructionStage());
    }
}
