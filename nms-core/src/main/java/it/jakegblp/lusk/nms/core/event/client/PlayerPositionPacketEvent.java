package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.RelativeFlag;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

@NullMarked
@Getter
public class PlayerPositionPacketEvent extends ClientPacketEvent<PlayerPositionPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int teleportId;
    protected Vector position;
    protected float yaw, pitch;
    @Availability(addedIn = "1.21.2")
    protected Vector velocity;
    protected Set<RelativeFlag> relativeFlags;
    protected World world;

    public PlayerPositionPacketEvent(PlayerPositionPacket packet, Player player, boolean async) {
        super(player, async);
        this.teleportId = packet.getTeleportId();
        this.position = packet.getPosition();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
        this.velocity = packet.getVelocity();
        this.relativeFlags = new HashSet<>(packet.getRelativeFlags());
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

    public void setPitch(float pitch) {
        markModified();
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
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

    public void setTeleportId(int entityId) {
        markModified();
        this.teleportId = entityId;
    }

    @Contract("-> new")
    public Location getLocation() {
        return position.toLocation(world, yaw, pitch);
    }

    public void setLocation(Location location) {
        markModified();
        position = location.toVector();
        yaw = location.getYaw();
        pitch = location.getPitch();
    }

    public void setRelativeFlags(Set<RelativeFlag> relativeFlags) {
        this.relativeFlags = new HashSet<>(relativeFlags);
    }

    @Contract("-> new")
    public Set<RelativeFlag> getRelativeFlags() {
        return new HashSet<>(relativeFlags);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public PlayerPositionPacket createPacket() {
        return new PlayerPositionPacket(getTeleportId(), getPosition(), getYaw(), getPitch(), getVelocity(), getRelativeFlags());
    }
}
