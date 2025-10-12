package it.jakegblp.lusk.nms.core.adapters;

import it.jakegblp.lusk.nms.core.protocol.packets.client.SetEquipmentPacket;

public interface SetEquipmentPacketAdapter<
        NMSSetEquipmentPacket
        > {
    NMSSetEquipmentPacket toNMSSetEquipmentPacket(SetEquipmentPacket from);

    SetEquipmentPacket fromNMSSetEquipmentPacket(NMSSetEquipmentPacket from);

    Class<NMSSetEquipmentPacket> getNMSSetEquipmentPacketClass();

    default boolean isNMSSetEquipmentPacket(Object object) {
        return getNMSSetEquipmentPacketClass().isInstance(object);
    }
}
