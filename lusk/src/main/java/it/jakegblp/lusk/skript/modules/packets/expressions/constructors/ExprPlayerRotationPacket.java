package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPlayerRotationPacket extends SimpleExpression<PlayerRotationPacket> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprPlayerRotationPacket.class, ExprPlayerRotationPacket::new, PlayerRotationPacket.class,
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
