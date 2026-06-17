package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.PacketWrapperEvent;

public interface ClientboundPacketWithEntityId<E extends PacketWrapperEvent<?>> extends BufferSerializableClientboundPacket<E> {
    int getEntityId();
}
