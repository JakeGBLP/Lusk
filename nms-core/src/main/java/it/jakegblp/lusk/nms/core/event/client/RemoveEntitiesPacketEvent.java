package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.RemoveEntitiesPacket;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public class RemoveEntitiesPacketEvent extends ClientPacketEvent<RemoveEntitiesPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected IntList entityIds;

    public RemoveEntitiesPacketEvent(RemoveEntitiesPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityIds = new IntArrayList(packet.getEntityIds());
    }

    public void setEntityIds(IntList entityIds) {
        this.entityIds = new IntArrayList(entityIds);
    }

    @Contract("-> new")
    public IntList getEntityIds() {
        return new IntArrayList(entityIds);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public RemoveEntitiesPacket createPacket() {
        return new RemoveEntitiesPacket(getEntityIds());
    }
}
