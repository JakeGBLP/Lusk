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
import it.jakegblp.lusk.nms.core.events.ParticleSendEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

@Name("Event - Particle Send")
@Description("Gets sent when a particle is sent to the player")
@Examples({"""
        on particle sent:
            if event-particle is damage indicator:
                cancel event
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
public class ParticlePacketEvent extends SkriptEvent {
    static {
        Skript.registerEvent("Particle Packet", ParticlePacketEvent.class, ParticleSendEvent.class, "particle (send|sent|received) [packet]")
                .description("Called when the client receives a particle packet")
                .examples("""
                        on particle sent:
                            if event-particle is damage indicator:
                                cancel event
                        """)
                .since("1.0.0");

        EventValues.registerEventValue(ParticleSendEvent.class, Particle.class, ParticleSendEvent::getBukkitParticle, EventValues.TIME_NOW);
        EventValues.registerEventValue(ParticleSendEvent.class, Integer.class, ParticleSendEvent::getCount, EventValues.TIME_NOW);
        EventValues.registerEventValue(ParticleSendEvent.class, Float.class, ParticleSendEvent::getMaxSpeed, EventValues.TIME_NOW);
        EventValues.registerEventValue(ParticleSendEvent.class, Vector.class, ParticleSendEvent::getOffset, EventValues.TIME_NOW);
        EventValues.registerEventValue(ParticleSendEvent.class, Location.class, ParticleSendEvent::getLocation, EventValues.TIME_NOW);
        EventValues.registerEventValue(ParticleSendEvent.class, World.class, ParticleSendEvent::getWorld, EventValues.TIME_NOW);
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
        return "particle packet";
    } // todo make this neater :D
}
