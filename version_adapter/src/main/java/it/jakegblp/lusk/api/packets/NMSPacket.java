package it.jakegblp.lusk.api.packets;

//todo: figure out
public abstract class NMSPacket<
        Packet,
        PacketType
        > {

    public abstract PacketType getPacketType();


}
