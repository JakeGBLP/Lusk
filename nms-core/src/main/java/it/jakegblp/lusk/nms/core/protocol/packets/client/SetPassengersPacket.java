package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.event.client.SetPassengersPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NullMarked
public class SetPassengersPacket implements BufferSerializableClientboundPacket<SetPassengersPacketEvent> {

    protected ProtocolEntityReference vehicle;
    protected List<ProtocolEntityReference> passengers;

    public SetPassengersPacket(int vehicle, @Nullable Collection<Integer> passengers) {
        this(ProtocolEntityReference.of(vehicle), passengers == null || passengers.isEmpty() ? null : CommonUtils.map(passengers, ProtocolEntityReference::of));
    }

    public SetPassengersPacket(int vehicle) {
        this(ProtocolEntityReference.of(vehicle));
    }

    public SetPassengersPacket(ProtocolEntityReference vehicle, @Nullable Collection<ProtocolEntityReference> passengers) {
        this.vehicle = vehicle;
        this.passengers = (passengers == null || passengers.isEmpty()) ? new ArrayList<>() : new ArrayList<>(passengers);
    }

    public SetPassengersPacket(ProtocolEntityReference vehicle) {
        this(vehicle, null);
    }

    public SetPassengersPacket(SimpleByteBuf byteBuf){
        read(byteBuf);
    }

    @Override
    public SetPassengersPacketEvent createEvent(Player player, boolean async) {
        return new SetPassengersPacketEvent(this, player, async);
    }

    @Override
    public SetPassengersPacket copy() {
        return new SetPassengersPacket(vehicle, passengers);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(vehicle.getId());
        buffer.writeVarIntArray(passengers.stream().mapToInt(ProtocolEntityReference::getId).toArray());
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.vehicle = ProtocolEntityReference.of(buffer.readInt());
        this.passengers = Arrays.stream(buffer.readVarIntArray()).mapToObj(ProtocolEntityReference::of).toList();
    }
}
