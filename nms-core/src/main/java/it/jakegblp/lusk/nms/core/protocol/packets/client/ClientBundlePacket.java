package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.event.client.PacketBundlePacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.serialization.codec.BufferCodec;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NullMarked
@ToString
public class ClientBundlePacket extends BundlePacket<ClientboundPacket<?>> implements ClientboundPacket<PacketBundlePacketEvent> {

    public ClientBundlePacket(List<ClientboundPacket<?>> packets) {
        super(packets);
    }

    public ClientBundlePacket(ClientboundPacket<?>... packets) {
        super(packets);
    }

    public ClientBundlePacket() {
        super();
    }

    @Override
    public PacketBundlePacketEvent createEvent(Player player, boolean async) {
        return new PacketBundlePacketEvent(this, player, async);
    }

    @Override
    public ClientBundlePacket copy() {
        return new ClientBundlePacket(CommonUtils.map(getPackets(), ClientboundPacket::copy));
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        throw new UnsupportedOperationException("Bundle packets cannot write to buffers.");
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        throw new UnsupportedOperationException("Bundle packets cannot be read from buffers.");
    }

    @Override
    public BufferCodec<ClientboundPacket<PacketBundlePacketEvent>> getCodec() {
        throw new UnsupportedOperationException("Bundle packets do not have codecs.");
    }
}
