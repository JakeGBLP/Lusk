package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.PacketWrapperEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.BufferSerializablePacket;

public interface BufferSerializableClientboundPacket<E extends PacketWrapperEvent<?>> extends BufferSerializablePacket, ClientboundPacket<E> {
}
