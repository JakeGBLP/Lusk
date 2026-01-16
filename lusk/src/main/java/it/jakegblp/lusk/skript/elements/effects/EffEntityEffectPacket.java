package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Keywords;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityEventPacket;
import it.jakegblp.lusk.nms.core.world.entity.effect.InternalEntityEffect;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Name("Player - Send Entity Effect Packets")
@Description("Send a entity effect packet to players")
@Examples({"""
        send entity effect sheep eat grass for entity target entity for all players
        send entity effect sheep eat grass for entity with id 12 for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
public class EffEntityEffectPacket extends Effect {

    static {
        Skript.registerEffect(EffEntityEffectPacket.class,
                "play entity effect %entityeffect/internalentityeffect% on %entities/numbers% for %players%"
        );
    }

    private Expression<Object> effectExpression;
    private Expression<Object> entityOrIDExpression;
    private Expression<Player> playerExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        effectExpression = (Expression<Object>) expressions[0];
        entityOrIDExpression = (Expression<Object>) expressions[1];
        playerExpression = (Expression<Player>) expressions[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Object effect = effectExpression.getSingle(event);
        byte data;
        if (effect instanceof EntityEffect entityEffect)
            data = entityEffect.getData();
        else if (effect instanceof InternalEntityEffect internalEntityEffect)
            data = internalEntityEffect.getData();
        else return;
        Set<EntityEventPacket> packets = new HashSet<>();
        for (Object o : entityOrIDExpression.getArray(event))
            if (o instanceof Entity entity)
                packets.add(new EntityEventPacket(entity.getEntityId(), data));
            else if (o instanceof Number number)
                packets.add(new EntityEventPacket(number.intValue(), data));
        NMSApi.sendPackets(playerExpression.getArray(event), packets.toArray(ClientboundPacket[]::new));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "play entity effect " + effectExpression.toString(event, debug) + " on " + entityOrIDExpression.toString(event, debug) + " for " + playerExpression.toString(event, debug);
    }
}
