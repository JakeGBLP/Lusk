package it.jakegblp.lusk.skript.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import it.jakegblp.lusk.nms.core.events.PacketReceiveEvent;
import it.jakegblp.lusk.nms.core.events.PacketSendEvent;
import it.jakegblp.lusk.nms.core.logger.events.LogEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ConsoleLogEvent extends SkriptEvent {
    static {
        Skript.registerEvent("Server Log", ConsoleLogEvent.class, LogEvent.class, "(console|server) log")
                .description("Called when something is printed to console")
                .since("2.0.0");
        EventValues.registerEventValue(LogEvent.class, String.class, LogEvent::getMessage, EventValues.TIME_NOW);
    }

    @Override
    public boolean canExecuteAsynchronously() {
        return true;
    }


    @Override
    public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "console / file log event";
    }
}
