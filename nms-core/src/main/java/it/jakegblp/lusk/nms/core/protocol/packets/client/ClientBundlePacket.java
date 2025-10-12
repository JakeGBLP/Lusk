package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@EqualsAndHashCode(callSuper = true)
@NullMarked
@ToString
public class ClientBundlePacket extends BundlePacket<ClientboundPacket> implements ClientboundPacket {

    public static final ClientBundlePacket[] EMPTY_ARRAY = new ClientBundlePacket[0];

    public ClientBundlePacket(
            List<ClientboundPacket> packets) {
        super(packets);
    }

    public ClientBundlePacket(ClientboundPacket... packets) {
        super(packets);
    }

    public ClientBundlePacket() {
        super();
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSClientBundlePacket(this);
    }
}
