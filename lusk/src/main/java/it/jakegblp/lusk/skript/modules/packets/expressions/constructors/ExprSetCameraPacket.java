package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SetCameraPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprSetCameraPacket extends SimpleExpression<SetCameraPacket> {

    private Expression<ProtocolEntityReference> protocolEntityReferenceExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprSetCameraPacket.class, ExprSetCameraPacket::new, SetCameraPacket.class)
                .patterns("[a|[a] new] [set] camera packet (with|using) %protocolentityreference%")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        protocolEntityReferenceExpression = (Expression<ProtocolEntityReference>) expressions[0];
        return true;
    }

    @Override
    protected SetCameraPacket @Nullable [] get(Event event) {
        var reference = protocolEntityReferenceExpression.getSingle(event);
        if (reference == null) return new SetCameraPacket[0];
        return new SetCameraPacket[]{new SetCameraPacket(reference.getId())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SetCameraPacket> getReturnType() {
        return SetCameraPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new set camera packet with " + protocolEntityReferenceExpression.toString(event, debug);
    }
}
