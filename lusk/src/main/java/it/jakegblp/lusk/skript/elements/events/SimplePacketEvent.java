package it.jakegblp.lusk.skript.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import it.jakegblp.lusk.nms.core.events.PacketReceiveEvent;
import it.jakegblp.lusk.nms.core.events.PacketSendEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class SimplePacketEvent extends SkriptEvent {
    static {
        Skript.registerEvent("Packet Receive", SimplePacketEvent.class, PacketReceiveEvent.class, "packet receive[d]")
                .description("Called when the server received a packet")
                .examples("on packet received:")
                .since("1.0.0");
        Skript.registerEvent("Packet Send", SimplePacketEvent.class, PacketSendEvent.class, "packet sen(d|t)")
                .description("Called when the server sends a packet")
                .examples("on packet sent:")
                .since("1.0.0");
        EventValues.registerEventValue(PacketReceiveEvent.class, ServerboundPacket.class, PacketReceiveEvent::getPacket, EventValues.TIME_NOW);
        EventValues.registerEventValue(PacketSendEvent.class, ClientboundPacket.class, PacketSendEvent::getPacket, EventValues.TIME_NOW);
    }

    @Override
    public boolean canExecuteAsynchronously() {
        return true;
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "every packet";
    }

}
