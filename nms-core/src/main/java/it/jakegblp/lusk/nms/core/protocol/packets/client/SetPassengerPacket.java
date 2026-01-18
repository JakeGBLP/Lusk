package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class SetPassengerPacket implements BufferSerializableClientboundPacket{

    private int vehicle;
    private int[] passengers;

    public SetPassengerPacket(SimpleByteBuf byteBuf){
        read(byteBuf);
    }

    @Override
    public Packet copy() {
        return new SetPassengerPacket(vehicle, passengers);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(vehicle);
        buffer.writeVarIntArray(passengers);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.vehicle = buffer.readInt();
        this.passengers = buffer.readVarIntArray();
    }
}
