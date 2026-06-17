package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityTeleportPacket;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
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
    protected Vector position;
    @Availability(addedIn = "1.21.2")
    protected Number yaw, pitch;
    protected Vector velocity;
    protected boolean onGround;
    protected World world;

    public EntityTeleportPacketEvent(EntityTeleportPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.position = packet.getPosition();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
        this.velocity = packet.getVelocity();
        this.onGround = packet.isOnGround();
        this.world = player.getWorld();
    }

    @Contract("-> new")
    public Vector getPosition() {
        return position.clone();
    }

    public void setPosition(Vector position) {
        markModified();
        this.position = position.clone();
    }

    public void setOnGround(boolean onGround) {
        markModified();
        this.onGround = onGround;
    }

    public void setPitch(Number pitch) {
        markModified();
        this.pitch = pitch;
    }

    public void setYaw(Number yaw) {
        markModified();
        this.yaw = yaw;
    }

    @Availability(addedIn = "1.21.2")
    public void setVelocity(Vector velocity) {
        markModified();
        this.velocity = velocity.clone();
    }

    @Availability(addedIn = "1.21.2")
    public Vector getVelocity() {
        return velocity;
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    @Contract("-> new")
    public Location getLocation() {
        return position.toLocation(world, yaw.floatValue(), pitch.floatValue());
    }

    public void setLocation(Location location) {
        markModified();
        position = location.toVector();
        yaw = location.getYaw();
        pitch = location.getPitch();
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
        return new EntityTeleportPacket(getEntityId(), getPosition(), getYaw().floatValue(), getPitch().floatValue(), getVelocity(), isOnGround());
    }
}
