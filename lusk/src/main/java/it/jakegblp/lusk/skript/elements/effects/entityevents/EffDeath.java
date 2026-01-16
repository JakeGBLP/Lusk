package it.jakegblp.lusk.skript.elements.effects.entityevents;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityEventPacket;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;


@Name("[NMS] Player - Fake Death")
@Description("""
        Fakes a death state of an entity
        """)
@Examples({"""
        fake display with id 12 billboard to center for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffDeath extends Effect {

    static {
        Skript.registerEffect(EffDeath.class,
                "(make|fake) [entity|entity with id] %entities/numbers%['s] death for %players%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        return true;
    }


    @Override
    protected void execute(Event event) {
        Set<EntityEventPacket> packets = new HashSet<>();

        for (Object o : entityOrId.getArray(event)) {
            if(o instanceof Entity entity)
                packets.add(new EntityEventPacket(entity.getEntityId(), EntityEffect.ENTITY_DEATH));
            else if (o instanceof Number number)
                packets.add(new EntityEventPacket(number.intValue(), EntityEffect.ENTITY_DEATH));
        }

        NMSApi.sendPackets(playerExpression.getArray(event), packets.toArray(ClientboundPacket[]::new));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "Fake death";
    }


}
