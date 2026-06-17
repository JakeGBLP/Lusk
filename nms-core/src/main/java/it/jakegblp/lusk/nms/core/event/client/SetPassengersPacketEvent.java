package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.SetPassengersPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
@Getter
public class SetPassengersPacketEvent extends ClientPacketEvent<SetPassengersPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected ProtocolEntityReference vehicle;
    protected List<ProtocolEntityReference> passengers;

    public SetPassengersPacketEvent(SetPassengersPacket packet, Player player, boolean async) {
        super(player, async);
        this.vehicle = packet.getVehicle();
        this.passengers = new ArrayList<>(packet.getPassengers());
    }

    @Contract("-> new")
    public List<ProtocolEntityReference> getPassengers() {
        return new ArrayList<>(this.passengers);
    }

    public void setPassengers(List<ProtocolEntityReference> passengers) {
        this.passengers = new ArrayList<>(passengers);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public SetPassengersPacket createPacket() {
        return new SetPassengersPacket(getVehicle(), getPassengers());
    }
}
