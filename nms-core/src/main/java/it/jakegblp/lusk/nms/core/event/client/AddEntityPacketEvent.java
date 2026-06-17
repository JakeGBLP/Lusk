package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.AddEntityPacket;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
@Getter
public class AddEntityPacketEvent extends ClientPacketEvent<AddEntityPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;
    protected UUID entityUUID;
    protected Vector position, velocity;
    protected float pitch, yaw, headYaw;
    protected EntityType entityType;
    protected int data;
    protected World world;

    public AddEntityPacketEvent(AddEntityPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.entityUUID = player.getUniqueId();
        this.position = packet.getPosition();
        this.velocity = packet.getVelocity();
        this.pitch = packet.getPitch();
        this.yaw = packet.getYaw();
        this.headYaw = packet.getHeadYaw();
        this.entityType = packet.getEntityType();
        this.data = packet.getData();
        this.world = player.getWorld();
    }

    @Contract("-> new")
    public Vector getPosition() {
        return position.clone();
    }

    @Contract("-> new")
    public Vector getVelocity() {
        return velocity.clone();
    }

    @Contract("-> new")
    public Location getLocation() {
        return position.toLocation(world, yaw, pitch);
    }

    public void setLocation(Location location) {
        markModified();
        this.position = location.toVector();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public void setPitch(float pitch) {
        markModified();
        this.pitch = pitch;
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    public void setData(int data) {
        markModified();
        this.data = data;
    }

    public void setEntityType(EntityType entityType) {
        markModified();
        this.entityType = entityType;
    }

    public void setEntityUUID(UUID entityUUID) {
        markModified();
        this.entityUUID = entityUUID;
    }

    public void setHeadYaw(float headYaw) {
        markModified();
        this.headYaw = headYaw;
    }

    public void setPosition(Vector position) {
        markModified();
        this.position = position;
    }

    public void setVelocity(Vector velocity) {
        markModified();
        this.velocity = velocity;
    }

    public void setYaw(float yaw) {
        markModified();
        this.yaw = yaw;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public AddEntityPacket createPacket() {
        return new AddEntityPacket(getEntityId(), getEntityUUID(), getLocation(), getHeadYaw(), getVelocity(), getEntityType(), getData());
    }
}
