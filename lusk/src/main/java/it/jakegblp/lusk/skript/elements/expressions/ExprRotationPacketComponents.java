package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import org.jetbrains.annotations.Nullable;

public class ExprRotationPacketComponents extends SimplePropertyExpression<PlayerRotationPacket, Float> {

    static {
        register(ExprRotationPacketComponents.class, Float.class, "[player] packet rotation (:yaw|pitch)", "playerrotationpackets");
    }

    private boolean yaw;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        yaw = parseResult.hasTag("yaw");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Float convert(PlayerRotationPacket from) {
        return yaw ? from.getYaw() : from.getPitch();
    }

    @Override
    protected String getPropertyName() {
        return "packet rotation " + (yaw ?  "yaw" : "pitch");
    }

    @Override
    public Class<? extends Float> getReturnType() {
        return Float.class;
    }
}