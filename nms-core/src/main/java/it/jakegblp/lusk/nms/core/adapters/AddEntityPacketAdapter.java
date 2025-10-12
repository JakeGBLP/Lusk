package it.jakegblp.lusk.nms.core.adapters;

import it.jakegblp.lusk.nms.core.protocol.packets.client.AddEntityPacket;

public interface AddEntityPacketAdapter<
        NMSAddEntityPacket
        > {
    NMSAddEntityPacket toNMSAddEntityPacket(AddEntityPacket from);

    AddEntityPacket fromNMSAddEntityPacket(NMSAddEntityPacket from);

    Class<NMSAddEntityPacket> getNMSAddEntityPacketClass();

    default boolean isNMSAddEntityPacket(Object object) {
        return getNMSAddEntityPacketClass().isInstance(object);
    }
}
