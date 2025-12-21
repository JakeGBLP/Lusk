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
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class BlockUpdateEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();


    float x;
    float y;
    float z;

    Material material;
    BlockData blockData;

    Block originalBlock;

    boolean isCancelled;

    public BlockUpdateEvent(@NotNull Player who, boolean async) {
        super(who, async);
    }

    public Location getLocation(){
        return new Location(player.getWorld(), x, y, z);
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

}
