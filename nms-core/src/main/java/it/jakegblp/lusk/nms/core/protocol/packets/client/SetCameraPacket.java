package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SetCameraPacket implements BufferSerializableClientboundPacket {
    protected int cameraId;

    public SetCameraPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(cameraId);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        cameraId = buffer.readVarInt();
    }

    @Override
    public SetCameraPacket copy() {
        return new SetCameraPacket(cameraId);
    }
}
