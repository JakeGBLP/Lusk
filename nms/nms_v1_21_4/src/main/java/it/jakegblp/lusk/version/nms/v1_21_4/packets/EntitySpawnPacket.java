package it.jakegblp.lusk.version.nms.v1_21_4.packets;

import it.jakegblp.lusk.api.packets.NMSPacket;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.UUID;

//todo: figure out what to do with this
public class EntitySpawnPacket extends NMSPacket<
        ClientboundAddEntityPacket,
        PacketType<ClientboundAddEntityPacket>
        > {

    public class Builder {
        private Integer id = null;
        private UUID uuid = null;
        private final EntityType type;
        private final Vector position;
        private Vector velocity;
        private float yaw, pitch;
        private double headRotation;
        private int data;

        public Builder(EntityType type, Vector position) {
            this.type = type;
            this.position = position;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }
        public Builder setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }
        public Builder setVelocity(Vector velocity) {
            this.velocity = velocity;
            return this;
        }
        public Builder setYaw(float yaw) {
            this.yaw = yaw;
            return this;
        }
        public Builder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }
        public Builder setHeadRotation(double headRotation) {
            this.headRotation = headRotation;
            return this;
        }
        public Builder setData(int data) {
            this.data = data;
            return this;
        }
        public EntitySpawnPacket build() {
            return new EntitySpawnPacket(
                    id,
                    uuid,
                    type,
                    position,
                    velocity,
                    yaw,
                    pitch,
                    headRotation,
                    data
            );
        }

    }

    private final int id;
    private final UUID uuid;
    private final EntityType type;
    private final Vector position, velocity;
    private final float yaw, pitch;
    private final double headRotation;
    private final int data;

    public EntitySpawnPacket(int id, UUID uuid, EntityType type, Vector position, Vector velocity, float yaw, float pitch, double headRotation, int data) {
        this.id = id;
        this.uuid = uuid;
        this.type = type;
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headRotation = headRotation;
        this.data = data;
    }

    @Override
    public PacketType<ClientboundAddEntityPacket> getPacketType() {
        return null;
    }

    //@Override
    //public ClientboundAddEntityPacket getPacket() {
    //    return new ClientboundAddEntityPacket(
    //            id,
    //            uuid,
    //            position.getX(),
    //            position.getY(),
    //            position.getZ(),
    //            pitch,
    //            yaw,
    //            headRotation,
    //            NMS_1_21_4.INSTANCE.getNMSEntityType(type),
    //            data,
    //            velocity.getX(),
    //            velocity.getY(),
    //            velocity.getZ()
    //    );
    //}
}
