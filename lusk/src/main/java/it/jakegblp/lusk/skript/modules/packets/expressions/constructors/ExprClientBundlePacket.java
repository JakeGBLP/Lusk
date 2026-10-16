package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientBundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.syntax.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

public class ExprClientBundlePacket extends SimpleExpression<ClientBundlePacket> implements AsyncableSyntaxesWrapper {

    private boolean empty;
    private Expression<? extends ClientboundPacket> packetExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprClientBundlePacket.class, ExprClientBundlePacket::new, ClientBundlePacket.class)
                .patterns("[a(n| new)] empty client (bundle packet|packet bundle)",
                        "[a] [new] [client] (bundle packet|packet bundle) (with|containing|from) %clientpackets%")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        empty = matchedPattern == 0;
        if (!empty) packetExpression = (Expression<? extends ClientboundPacket>) expressions[0];
        return true;
    }

    @Override
    protected ClientBundlePacket @Nullable [] get(Event event) {
        if (empty) return new ClientBundlePacket[]{new ClientBundlePacket()};
        else if (packetExpression == null) return new ClientBundlePacket[0];
        else return new ClientBundlePacket[]{new ClientBundlePacket(packetExpression.getAll(event))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<ClientBundlePacket> getReturnType() {
        return ClientBundlePacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new " + (empty ? "empty " : "") + "client packet bundle" + (empty ? " with " + packetExpression.toString(event, debug) : "");
    }

    @Override
    public List<AsyncableSyntax> getAsyncableSyntaxes() {
        return AsyncableSyntaxesWrapper.filterAsyncableSyntaxes(AddonUtils.spread(packetExpression));
    }
}
