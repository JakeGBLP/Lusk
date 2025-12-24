package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Spawn_Entity">Entity Spawn Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode // todo: move headYaw field to earlier in the constructors
@AllArgsConstructor
public class AddEntityPacket implements ClientboundPacketWithId {

    protected int id;
    protected @NotNull UUID entityUUID;
    protected double x, y, z;
    protected float pitch, yaw, headYaw;
    protected @NotNull Vector velocity;
    protected @NotNull EntityType entityType;
    protected int data;

    public AddEntityPacket(SimpleByteBuf buffer) {
        this(buffer.readVarInt(),
                buffer.readUUID(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readPackedDegrees(),
                buffer.readPackedDegrees(),
                buffer.readPackedDegrees(),
                buffer.readVector(),
                buffer.readEntityType(),
                buffer.readVarInt());
    }

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            @NotNull Location location,
            float headYaw,
            @NotNull Vector velocity,
            @NotNull EntityType entityType,
            int data) {
        this(id, entityUUID, location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), headYaw, velocity, entityType, data);
    }

    public AddEntityPacket(
            int id,
            @NotNull UUID entityUUID,
            @NotNull Vector position,
            float headYaw,
            @NotNull Vector velocity,
            @NotNull EntityType entityType,
            int data) {
        this(id, entityUUID, position.getX(), position.getY(), position.getZ(), 0, 0, headYaw, velocity, entityType, data);
    }

    @Override
    public Object asNMS() {
        return NMS.toNMS(this);
    }

    /**
     * @return a location where the world is always null
     */
    public Location getPosition() {
        return new Location(null, x, y, z, yaw, pitch);
    }

    // todo: finish
    @Override
    protected AddEntityPacket clone() throws CloneNotSupportedException {
        return (AddEntityPacket) super.clone();
    }
}
