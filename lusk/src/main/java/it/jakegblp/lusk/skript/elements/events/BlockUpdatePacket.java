package it.jakegblp.lusk.skript.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import it.jakegblp.lusk.nms.core.events.BlockUpdateEvent;
import it.jakegblp.lusk.nms.core.events.ParticleSendEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class BlockUpdatePacket extends SkriptEvent {
    static {
        Skript.registerEvent("Block Update Packet", BlockUpdatePacket.class, BlockUpdateEvent.class, "block (send|change|update) [packet]")
                .description("Called when the client receives a block update packet")
                .examples("""
                        on block change packet:
                            if {farmLocs::*} contains event-location:
                                cancel event
                        """)
                .since("2.0.0");

        EventValues.registerEventValue(BlockUpdateEvent.class, Material.class, BlockUpdateEvent::getMaterial, EventValues.TIME_NOW);
        EventValues.registerEventValue(BlockUpdateEvent.class, BlockData.class, BlockUpdateEvent::getBlockData, EventValues.TIME_NOW);
        EventValues.registerEventValue(BlockUpdateEvent.class, Location.class, BlockUpdateEvent::getLocation, EventValues.TIME_NOW);
        EventValues.registerEventValue(BlockUpdateEvent.class, World.class, e -> e.getLocation().getWorld(), EventValues.TIME_NOW);
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
        return "block update packet";
    } // todo make this neater :D
}
