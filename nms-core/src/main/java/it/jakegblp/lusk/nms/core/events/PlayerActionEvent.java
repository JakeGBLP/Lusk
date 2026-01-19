package it.jakegblp.lusk.nms.core.events;

import io.papermc.paper.math.BlockPosition;
import it.jakegblp.lusk.nms.core.protocol.packets.server.PlayerActionPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;


@Getter
@Setter
public class PlayerActionEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    boolean isCancelled;

    BlockFace direction;
    PlayerActionPacket.Action action;
    int sequence;

    BlockPosition blockPos;


    public PlayerActionEvent(@NotNull Player who, boolean async) {
        super(who, async);
    }

    @SuppressWarnings("UnstableApiUsage")
    public Location getLocation(){
        return new Location(player.getWorld(), blockPos.x(), blockPos.y(), blockPos.z());
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
