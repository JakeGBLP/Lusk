package it.jakegblp.lusk.nms.core.adapters;

import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;

public interface EntityMetadataPacketAdapter<NMSEntityMetadataPacket> {
    NMSEntityMetadataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from);

    EntityMetadataPacket fromNMSEntityMetadataPacket(NMSEntityMetadataPacket from);

    Class<NMSEntityMetadataPacket> getNMSEntityMetadataPacketClass();

    default boolean isNMSEntityMetadataPacket(Object object) {
        return getNMSEntityMetadataPacketClass().isInstance(object);
    }
}