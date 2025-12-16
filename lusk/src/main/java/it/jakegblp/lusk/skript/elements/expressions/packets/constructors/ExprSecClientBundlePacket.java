package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientBundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExprSecClientBundlePacket extends SimpleExpression<ClientBundlePacket> implements AsyncableSyntaxesWrapper {

    static {
        Skript.registerExpression(ExprSecClientBundlePacket.class, ClientBundlePacket.class, ExpressionType.COMBINED,
                "[a(n| new)] empty client (bundle packet|packet bundle)",
                "[a] [new] [client] (bundle packet|packet bundle) (with|containing|from) %clientpackets%");
    }

    private boolean empty;
    private Expression<? extends ClientboundPacket> packetExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        empty = matchedPattern == 0;
        if (!empty) packetExpression = (Expression<? extends ClientboundPacket>) expressions[0];
        return true;
    }

    @Override
    protected ClientBundlePacket @Nullable [] get(Event event) {
        if (empty) return new ClientBundlePacket[] {new ClientBundlePacket()};
        else if (packetExpression == null) return new ClientBundlePacket[0];
        return new ClientBundlePacket[] {new ClientBundlePacket(packetExpression.getAll(event))};
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
