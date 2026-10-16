package it.jakegblp.lusk.nms.core.protocol.packets;

public interface PacketWithRotation {

    float getYaw();
    float getPitch();

    void setYaw(float yaw);
    void setPitch(float pitch);
}
