package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@AllArgsConstructor
@Setter
@Getter
public class SetCameraPacket implements ClientboundPacket {
    protected int cameraId;

    @Override
    public Object asNMS() {
        return NMS.toNMSSetCameraPacket(this);
    }

    @Override
    public SetCameraPacket clone() throws CloneNotSupportedException {
        return (SetCameraPacket) super.clone();
    }
}
