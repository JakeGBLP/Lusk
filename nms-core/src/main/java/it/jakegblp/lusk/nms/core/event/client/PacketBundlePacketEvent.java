package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientBundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class PacketBundlePacketEvent extends ClientPacketEvent<ClientBundlePacket> {

    private static final HandlerList handlers = new HandlerList();

    protected List<ClientboundPacket<?>> packets;

    public PacketBundlePacketEvent(ClientBundlePacket packet, Player player, boolean async) {
        super(player, async);
        this.packets = NullabilityUtils.copyIfNotNull(packet.getPackets());
    }

    @Contract("-> new")
    public List<ClientboundPacket<?>> getPackets() {
        return NullabilityUtils.copyIfNotNull(this.packets);
    }

    public void setPackets(List<ClientboundPacket<?>> packets) {
        markModified();
        this.packets = NullabilityUtils.copyIfNotNull(packets);
    }

    @Override
    public ClientBundlePacket createPacket() {
        return new ClientBundlePacket(getPackets());
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
