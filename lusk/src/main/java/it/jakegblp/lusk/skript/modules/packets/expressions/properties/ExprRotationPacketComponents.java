package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprRotationPacketComponents extends SimplePropertyExpression<PlayerRotationPacket, Number> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerPropertyExpression(syntaxRegistry, ExprRotationPacketComponents.class, ExprRotationPacketComponents::new, Number.class,
                "[player] packet rotation (:yaw|pitch)", "playerrotationpackets");
    }

    private boolean yaw;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        yaw = parseResult.hasTag("yaw");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Number convert(PlayerRotationPacket from) {
        return yaw ? from.getYaw() : from.getPitch();
    }

    @Override
    protected String getPropertyName() {
        return "packet rotation " + (yaw ?  "yaw" : "pitch");
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }
}