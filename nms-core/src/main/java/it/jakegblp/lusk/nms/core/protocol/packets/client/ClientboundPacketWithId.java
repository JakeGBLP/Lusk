package it.jakegblp.lusk.nms.core.protocol.packets.client;

public interface ClientboundPacketWithId extends ClientboundPacket {
    int getEntityId();
}
