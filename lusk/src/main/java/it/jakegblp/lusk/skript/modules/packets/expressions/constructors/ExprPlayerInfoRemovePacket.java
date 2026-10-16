package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoRemovePacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;
import java.util.UUID;

public class ExprPlayerInfoRemovePacket extends SimpleExpression<PlayerInfoRemovePacket> {

    private Expression<UUID> uuidExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprPlayerInfoRemovePacket.class, ExprPlayerInfoRemovePacket::new, PlayerInfoRemovePacket.class)
                .patterns("[a|[a] new] player info[s] remov(e|al) packet (with|using) %uuids%")
                .register(syntaxRegistry);
    }

    @Override
    protected PlayerInfoRemovePacket @Nullable [] get(Event event) {
        return new PlayerInfoRemovePacket[]{new PlayerInfoRemovePacket(List.of(uuidExpression.getArray(event)))};
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        uuidExpression = (Expression<UUID>) expressions[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PlayerInfoRemovePacket> getReturnType() {
        return PlayerInfoRemovePacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new player info remove packet with " + uuidExpression.toString(event, debug);
    }
}
