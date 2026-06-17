package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.RemoveEntitiesPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprRemoveEntitiesPacket extends SimpleExpression<RemoveEntitiesPacket> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprRemoveEntitiesPacket.class, ExprRemoveEntitiesPacket::new, RemoveEntitiesPacket.class,
                "[a|[a] new] remove entit(ies|y) packet (with|using) %protocolentityreference%",
                "[an|[a] new] entit(ies|y) remov(e|al) packet (with|using) %protocolentityreference%");
    }

    private Expression<ProtocolEntityReference> protocolEntityReferenceExpression;

    @Override
    protected RemoveEntitiesPacket @Nullable [] get(Event event) {
        return new RemoveEntitiesPacket[] { new RemoveEntitiesPacket(CommonUtils.mapToList(
                protocolEntityReferenceExpression.getArray(event),
                ProtocolEntityReference::getId))
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        protocolEntityReferenceExpression = (Expression<ProtocolEntityReference>) expressions[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends RemoveEntitiesPacket> getReturnType() {
        return RemoveEntitiesPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new remove entities packet with " + protocolEntityReferenceExpression.toString(event, debug);
    }
}
