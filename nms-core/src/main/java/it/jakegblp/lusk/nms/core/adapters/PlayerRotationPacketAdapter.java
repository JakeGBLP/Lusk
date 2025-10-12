package it.jakegblp.lusk.nms.core.adapters;

import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;

public interface PlayerRotationPacketAdapter {
    Object toNMSPlayerRotationPacket(PlayerRotationPacket from);

    PlayerRotationPacket fromNMSPlayerRotationPacket(Object from);

    Class<?> getNMSPlayerRotationPacketClass();

    default boolean isNMSPlayerRotationPacket(Object object) {
        return getNMSPlayerRotationPacketClass().isInstance(object);
    }
}
