package it.jakegblp.lusk.api.packets;

import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.UUID;

public abstract class NMSEntitySpawnPacket<
        Packet,
        PacketType
        > {

    public Integer id = null;
    public UUID uuid;
    public EntityType type;
    public Vector position, velocity;
    public float yaw, pitch;
    public double headRotation;
    public int data;

    public NMSEntitySpawnPacket<Packet, PacketType> setId(int id) {
        this.id = id;
        return this;
    }

    public NMSEntitySpawnPacket<Packet, PacketType> setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public NMSEntitySpawnPacket<Packet, PacketType> setPosition(Vector position) {
        this.position = position;
        return this;
    }

    public NMSEntitySpawnPacket<Packet, PacketType> setVelocity(Vector velocity) {
        this.velocity = velocity;
        return this;
    }

    public NMSEntitySpawnPacket<Packet, PacketType> setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public NMSEntitySpawnPacket<Packet, PacketType> setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public NMSEntitySpawnPacket<Packet, PacketType> setHeadRotation(double headRotation) {
        this.headRotation = headRotation;
        return this;
    }

    public NMSEntitySpawnPacket<Packet, PacketType> setData(int data) {
        this.data = data;
        return this;
    }

    public abstract Packet getPacket();
}
