package it.jakegblp.lusk.skript.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import it.jakegblp.lusk.nms.core.events.PacketReceiveEvent;
import it.jakegblp.lusk.nms.core.events.PacketSendEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;

public class SimplePacketEvents {
    static {
        Skript.registerEvent("Packet Receive", SimpleEvent.class, PacketReceiveEvent.class, "packet receive[d]")
                .description("Called when the server received a packet")
                .examples("on packet received:")
                .since("1.0.0");
        Skript.registerEvent("Packet Send", SimpleEvent.class, PacketSendEvent.class, "packet sen(d|t)")
                .description("Called when the server sends a packet")
                .examples("on packet sent:")
                .since("1.0.0");
        EventValues.registerEventValue(PacketReceiveEvent.class, ServerboundPacket.class, PacketReceiveEvent::getPacket, EventValues.TIME_NOW);
        EventValues.registerEventValue(PacketSendEvent.class, ClientboundPacket.class, PacketSendEvent::getPacket, EventValues.TIME_NOW);
    }
}
