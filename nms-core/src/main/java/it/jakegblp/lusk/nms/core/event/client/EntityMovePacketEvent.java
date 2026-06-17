package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.GenericMovementPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericRotationPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.MoveEntityPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EntityMovePacketEvent extends ClientPacketEvent<MoveEntityPacket> {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    protected int entityId;
    @Getter
    protected boolean onGround;
    protected boolean hasMovement, hasRotation;

    protected Vector movement = new Vector();
    @Getter
    protected float yaw, pitch;

    public EntityMovePacketEvent(MoveEntityPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.onGround = packet.isOnGround();
        if (packet.hasMovement()) {
            var positionPacket = (GenericMovementPacket) packet;
            this.hasMovement = true;
            this.movement = positionPacket.getMovement();
        }
        if (packet.hasRotation()) {
            var positionPacket = (GenericRotationPacket) packet;
            this.hasRotation = true;
            this.yaw = positionPacket.getYaw();
            this.pitch = positionPacket.getPitch();
        }
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    public void setYaw(float yaw) {
        markModified();
        this.yaw = yaw;
        this.hasRotation |= yaw == 0;
    }

    public void setPitch(float pitch) {
        markModified();
        this.pitch = pitch;
        this.hasRotation |= pitch == 0;
    }

    public void setMovement(Vector movement) {
        markModified();
        this.movement = movement.clone();
    }

    public void setOnGround(boolean onGround) {
        markModified();
        this.onGround = onGround;
    }

    @Contract("-> new")
    public Vector getMovement() {
        return movement.clone();
    }

    public boolean hasMovement() {
        return hasMovement;
    }

    public boolean hasRotation() {
        return hasRotation;
    }

    @Override
    public MoveEntityPacket createPacket() {
        if (hasRotation)
            if (hasMovement)
                return new MoveEntityPacket.PositionRotation(entityId, movement, yaw, pitch, onGround);
            else
                return new MoveEntityPacket.Rotation(entityId, yaw, pitch, onGround);
        else
            return new MoveEntityPacket.Position(entityId, movement, onGround);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
