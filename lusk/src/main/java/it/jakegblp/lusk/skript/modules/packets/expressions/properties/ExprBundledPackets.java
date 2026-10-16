package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprBundledPackets extends PropertyExpression<BundlePacket<?>, Packet> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBundledPackets.class, ExprBundledPackets::new, Packet.class)
                .propertyOf("bundled packets", "bundlepackets")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    protected Packet[] get(Event event, BundlePacket[] source) {
        return CommonUtils.flatMap(BundlePacket.class, source, BundlePacket::getPackets);
    }

    @Override
    public Class<? extends Packet> getReturnType() {
        return Packet.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "bundle packets of " + getExpr().toString(event, debug);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends BundlePacket<?>>) expressions[0]);
        return true;
    }
}

