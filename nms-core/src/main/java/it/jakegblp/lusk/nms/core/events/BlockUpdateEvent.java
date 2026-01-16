package it.jakegblp.lusk.nms.core.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@NullMarked
public class BlockUpdateEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected float x;
    protected float y;
    protected float z;

    protected Material material;
    protected BlockData blockData;

    protected Block originalBlock;

    boolean cancelled;

    public BlockUpdateEvent(Player player, float x, float y, float z, Material material, BlockData blockData, Block originalBlock, boolean cancelled) {
        super(player);
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
        this.blockData = blockData;
        this.originalBlock = originalBlock;
        this.cancelled = cancelled;
    }

    public BlockUpdateEvent(Player player, boolean async, float x, float y, float z, Material material, BlockData blockData, Block originalBlock, boolean cancelled) {
        super(player, async);
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
        this.blockData = blockData;
        this.originalBlock = originalBlock;
        this.cancelled = cancelled;
    }

    public BlockUpdateEvent(Player who, boolean async) {
        super(who, async);
    }

    @Contract(pure = true)
    public Location getLocation(){
        return new Location(player.getWorld(), x, y, z);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
