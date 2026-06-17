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
        var packets = NullabilityUtils.copyIfNotNull(packet.getPackets());
        assert packets != null;
        this.packets = packets;
    }

    @Contract("-> new")
    public List<ClientboundPacket<?>> getPackets() {
        var packets = NullabilityUtils.copyIfNotNull(this.packets);
        assert packets != null;
        return packets;
    }

    public void setPackets(List<ClientboundPacket<?>> packets) {
        markModified();
        var copiedPackets = NullabilityUtils.copyIfNotNull(packets);
        assert copiedPackets != null;
        this.packets = copiedPackets;
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
