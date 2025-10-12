package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ExprBundlePackets extends PropertyExpression<BundlePacket<?>, Packet> {

    static {
        register(ExprBundlePackets.class, Packet.class, "bundle[d] packets", "bundlepackets");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Packet[] get(Event event, BundlePacket[] source) {
        return (Packet[]) Arrays.stream(source).flatMap(bundlePacket -> bundlePacket.getPackets().stream()).toArray(Packet[]::new);
    }

    @Override
    public Class<? extends Packet> getReturnType() {
        return Packet.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "bundle packets of "+getExpr().toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends BundlePacket<?>>) expressions[0]);
        return true;
    }
}

