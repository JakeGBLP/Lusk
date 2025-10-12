package it.jakegblp.lusk.skript.elements.expressions.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientBundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.skript.api.OptionallySectionExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static it.jakegblp.lusk.skript.utils.Utils.parseSectionNodes;

public class ExprSecClientBundlePacket extends OptionallySectionExpression<ClientBundlePacket> {

    static {
        Skript.registerExpression(ExprSecClientBundlePacket.class, ClientBundlePacket.class, ExpressionType.COMBINED,
                "[a(n| new)] empty (bundle packet|packet bundle)",
                "[a] new [client] (bundle packet|packet bundle)",
                "[a] [new] [client] (bundle packet|packet bundle) (with|containing) [packet[s]] %clientpackets%");
    }

    private boolean empty;
    private ExpressionList<? extends ClientboundPacket> packetExpressionList;

    @Override
    public boolean hasSection(int pattern, SkriptParser.ParseResult result) {
        return pattern == 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean initNormal(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        empty = pattern == 0;
        if (!empty)
            packetExpressionList = new ExpressionList<ClientboundPacket>(new Expression[] {expressions[0]}, ClientboundPacket.class, true);
        return true;
    }

    @Override
    public boolean initSection(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        if (node == null) {
            Skript.error("Empty bundle packet section.");
            return false;
        }
        packetExpressionList = parseSectionNodes(this.getAsSection(), node, ClientboundPacket.class);
        if (packetExpressionList == null) {
            Skript.error("Parsed empty bundle packet.");
            return false;
        }
        return true;
    }

    @Override
    protected ClientBundlePacket @Nullable [] get(Event event) {
        if (empty) return new ClientBundlePacket[] {new ClientBundlePacket()};
        if (packetExpressionList == null) return ClientBundlePacket.EMPTY_ARRAY;
        return new ClientBundlePacket[] {new ClientBundlePacket(packetExpressionList.getAll(event))};
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
        return "a new " + (empty ? "empty " : "") + "client bundle packet" + (hasSection() ? " with packets " + packetExpressionList.toString(event, debug) : "");
    }
}
