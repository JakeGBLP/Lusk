package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityTeleportPacket;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public class EntityTeleportPacketEvent extends ClientPacketEvent<EntityTeleportPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;
    protected Location location;
    protected Vector velocity;
    protected boolean onGround;

    public EntityTeleportPacketEvent(EntityTeleportPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.location = packet.getPosition().toLocation(player.getWorld(), packet.getYaw(), packet.getPitch());
        this.velocity = packet.getVelocity();
        this.onGround = packet.isOnGround();
    }

    public EntityTeleportPacketEvent(Player player, int entityId, Location location, Vector velocity, boolean onGround, boolean async) {
        super(player, async);
        this.entityId = entityId;
        this.location = location;
        this.velocity = velocity;
        this.onGround = onGround;
    }

    @Contract("-> new")
    public Location getLocation() {
        return location.clone();
    }

    public void setOnGround(boolean onGround) {
        markModified();
        this.onGround = onGround;
    }

    public void setVelocity(Vector velocity) {
        markModified();
        this.velocity = velocity.clone();
    }

    public Vector getVelocity() {
        return velocity.clone();
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    public void setLocation(Location location) {
        markModified();
        this.location = location.clone();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public EntityTeleportPacket createPacket() {
        return new EntityTeleportPacket(getEntityId(), location.toVector(), location.getYaw(), location.getPitch(), getVelocity(), isOnGround());
    }
}
