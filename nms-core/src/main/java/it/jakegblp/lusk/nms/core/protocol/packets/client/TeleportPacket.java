package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.Getter;
import lombok.Setter;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
public class TeleportPacket implements ClientboundPacket{

    //todo: when adding the old teleport packet for older versions remember to convert yaw and pitch (pitch = (pitch * 256.0F / 360.0F);)

    int entityID;
    double x, y, z;
    float yaw;
    float pitch;
    boolean onGround;


    public TeleportPacket(int entityID, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    @Override
    public Object asNMS() {
        return NMS.isNMSTeleportPacket(this);
    }
}
