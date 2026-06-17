package it.jakegblp.lusk.nms.core.protocol.packets;

public interface GenericRotationPacket {

    float getYaw();
    float getPitch();

    void setYaw(float yaw);
    void setPitch(float pitch);
}
