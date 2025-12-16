package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPlayerRotationPacket extends SimpleExpression<PlayerRotationPacket> {

    static {
        Skript.registerExpression(ExprPlayerRotationPacket.class, PlayerRotationPacket.class, ExpressionType.COMBINED,
                "[a] new player rotation packet with yaw %number%[,] and [with] pitch %number%");
    }

    private Expression<Number> yawExpression, pitchExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        yawExpression = (Expression<Number>) expressions[0];
        pitchExpression = (Expression<Number>) expressions[1];
        return true;
    }

    @Override
    protected PlayerRotationPacket @Nullable [] get(Event event) {
        Number yaw = yawExpression.getSingle(event);
        if (yaw == null) return new PlayerRotationPacket[0];
        Number pitch = pitchExpression.getSingle(event);
        if (pitch == null) return new PlayerRotationPacket[0];
        return new PlayerRotationPacket[] { new PlayerRotationPacket(yaw.floatValue(), pitch.floatValue()) };
    }
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PlayerRotationPacket> getReturnType() {
        return PlayerRotationPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "new player rotation packet with yaw "+yawExpression.toString(event, debug) + " and with pitch "+pitchExpression.toString();
    }
}
