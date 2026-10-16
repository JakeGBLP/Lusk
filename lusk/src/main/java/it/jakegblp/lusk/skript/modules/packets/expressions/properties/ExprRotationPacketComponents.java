package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithRotation;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprRotationPacketComponents extends SimplerPropertyExpression<PacketWithRotation, Float> {

    private boolean yaw;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprRotationPacketComponents.class, ExprRotationPacketComponents::new, Float.class)
                .propertyOf("packet (:yaw|pitch)", "genericrotationpacket")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        yaw = parseResult.hasTag("yaw");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Float convert(PacketWithRotation from) {
        return yaw ? from.getYaw() : from.getPitch();
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[] {Float.class};
            case DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void set(PacketWithRotation from, Float to) {
        if (yaw)
            from.setYaw(to);
        else
            from.setPitch(to);
    }

    @Override
    public void add(PacketWithRotation from, Float to) {
        set(from, convert(from) + to);
    }

    @Override
    public void remove(PacketWithRotation from, Float to) {
        add(from, -to);
    }

    @Override
    public void delete(PacketWithRotation from) {
        set(from, 0f);
    }

    @Override
    protected String getPropertyName() {
        return "packet " + (yaw ? "yaw" : "pitch");
    }

    @Override
    public Class<? extends Float> getReturnType() {
        return Float.class;
    }
}