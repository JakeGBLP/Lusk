package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NullMarked
@ToString
public class ClientBundlePacket extends BundlePacket<ClientboundPacket> implements ClientboundPacket {

    public ClientBundlePacket(List<ClientboundPacket> packets) {
        super(packets);
    }

    public ClientBundlePacket(ClientboundPacket... packets) {
        super(packets);
    }

    public ClientBundlePacket() {
        super();
    }

    @Override
    public ClientBundlePacket copy() {
        var copy = new ClientBundlePacket();
        getPackets().forEach(clientboundPacket -> copy.add(NullabilityUtils.copyIfNotNull(clientboundPacket)));
        return copy;
    }
}
