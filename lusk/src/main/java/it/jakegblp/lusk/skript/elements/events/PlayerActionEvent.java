package it.jakegblp.lusk.skript.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Keywords;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import it.jakegblp.lusk.nms.core.events.BlockUpdateEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.server.PlayerActionPacket;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Event - Player Action Event")
@Description("Gets sent an action is sent from the player")
@Examples("""
        on player action:
            broadcast "s: %event-int%"
            broadcast "a: %event-nmsplayeraction%"
            broadcast "d: %event-blockface%"
            broadcast "l: %event-location%"
        """)
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
public class PlayerActionEvent extends SkriptEvent {
    static {
        Skript.registerEvent("Player Action Packet", PlayerActionEvent.class, it.jakegblp.lusk.nms.core.events.PlayerActionEvent.class, "player action [packet]")
                .description("Gets sent an action is sent from the player")
                .since("2.0.0");

        EventValues.registerEventValue(it.jakegblp.lusk.nms.core.events.PlayerActionEvent.class, Location.class, it.jakegblp.lusk.nms.core.events.PlayerActionEvent::getLocation, EventValues.TIME_NOW);
        EventValues.registerEventValue(it.jakegblp.lusk.nms.core.events.PlayerActionEvent.class, PlayerActionPacket.Action.class, it.jakegblp.lusk.nms.core.events.PlayerActionEvent::getAction, EventValues.TIME_NOW);
        EventValues.registerEventValue(it.jakegblp.lusk.nms.core.events.PlayerActionEvent.class, Integer.class, it.jakegblp.lusk.nms.core.events.PlayerActionEvent::getSequence, EventValues.TIME_NOW);
        EventValues.registerEventValue(it.jakegblp.lusk.nms.core.events.PlayerActionEvent.class, BlockFace.class, it.jakegblp.lusk.nms.core.events.PlayerActionEvent::getDirection, EventValues.TIME_NOW);
        EventValues.registerEventValue(it.jakegblp.lusk.nms.core.events.PlayerActionEvent.class, World.class, e -> e.getLocation().getWorld(), EventValues.TIME_NOW);
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
        return "player action event";
    }
}
