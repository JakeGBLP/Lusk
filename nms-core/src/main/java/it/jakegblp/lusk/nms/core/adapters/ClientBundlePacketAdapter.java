package it.jakegblp.lusk.nms.core.adapters;

import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientBundlePacket;

public interface ClientBundlePacketAdapter<
        NMSClientBundlePacket
        > {
    NMSClientBundlePacket toNMSClientBundlePacket(ClientBundlePacket from);

    ClientBundlePacket fromNMSClientBundlePacket(NMSClientBundlePacket from);

    Class<NMSClientBundlePacket> getNMSClientBundlePacketClass();

    default boolean isNMSClientBundlePacket(Object object) {
        return getNMSClientBundlePacketClass().isInstance(object);
    }
}
