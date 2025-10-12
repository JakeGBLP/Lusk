package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.world.entity.flags.entity.RelativeFlag;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
public class PlayerPositionPacket implements ClientboundPacket {

    protected int teleportId;
    protected Vector position;
    @Availability(addedIn = "1.21.2")
    protected Vector velocity;
    protected float yaw, pitch;
    protected Set<@NotNull RelativeFlag> relativeFlags;

    public PlayerPositionPacket(int teleportId, @Nullable Vector position, @Availability(addedIn = "1.21.2") @Nullable Vector velocity, float yaw, float pitch, Set<@NotNull RelativeFlag> relativeFlags) {
        this.teleportId = teleportId;
        this.position = position;
        this.velocity = velocity == null ? new Vector() : velocity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
    }

    public PlayerPositionPacket(int teleportId, @Nullable Vector position, float yaw, float pitch, Set<@NotNull RelativeFlag> relativeFlags) {
        this(teleportId, position, new Vector(), yaw, pitch, relativeFlags);
    }

    @Availability(addedIn = "1.21.2")
    public Vector getVelocity() {
        return velocity;
    }

    @Availability(addedIn = "1.21.2")
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public double getX() {
        return position.getX();
    }
    public double getY() {
        return position.getY();
    }
    public double getZ() {
        return position.getZ();
    }
    @Availability(addedIn = "1.21.2")
    public double getVelocityX() {
        return velocity.getX();
    }
    @Availability(addedIn = "1.21.2")
    public double getVelocityY() {
        return velocity.getY();
    }
    @Availability(addedIn = "1.21.2")
    public double getVelocityZ() {
        return velocity.getZ();
    }

    public void setX(double x) {
        position.setX(x);
    }
    public void setY(double y) {
        position.setY(y);
    }
    public void setZ(double z) {
        position.setZ(z);
    }
    @Availability(addedIn = "1.21.2")
    public void setVelocityX(double x) {
        velocity.setX(x);
    }
    @Availability(addedIn = "1.21.2")
    public void setVelocityY(double y) {
        velocity.setY(y);
    }
    @Availability(addedIn = "1.21.2")
    public void setVelocityZ(double z) {
        velocity.setZ(z);
    }

    @Override
    public Object asNMS() {
        return NMS.asNMSPlayerPositionPacket(this);
    }
}
