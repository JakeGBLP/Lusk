package it.jakegblp.lusk.skript.modules.packets.effects.entityeffects;

import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityEventPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

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

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerEffect(syntaxRegistry, EffDeath.class, EffDeath::new, "kill %protocolentityreferences% for %players%");
    }

    private Expression<ProtocolEntityReference> protocolEntityReferenceExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        protocolEntityReferenceExpression = (Expression<ProtocolEntityReference>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        return true;
    }


    @Override
    protected void execute(Event event) {
        Set<EntityEventPacket> packets = new HashSet<>();
        for (var protocolEntityReference : protocolEntityReferenceExpression.getArray(event))
            packets.add(new EntityEventPacket(protocolEntityReference.getId(), EntityEffect.ENTITY_DEATH));
        NMSApi.sendPackets(playerExpression.getArray(event), packets.toArray(ClientboundPacket[]::new));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "kill " + protocolEntityReferenceExpression.toString(event, debug) + " for " + playerExpression.toString(event, debug);
    }

}
