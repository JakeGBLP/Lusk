package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.SetCameraPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Setter
@Getter
public class SetCameraPacket implements BufferSerializableClientboundPacket<SetCameraPacketEvent> {
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
    public SetCameraPacketEvent createEvent(Player player, boolean async) {
        return new SetCameraPacketEvent(this, player, async);
    }

    @Override
    public SetCameraPacket copy() {
        return new SetCameraPacket(cameraId);
    }
}
