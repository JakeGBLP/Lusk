package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import it.jakegblp.lusk.nms.core.protocol.packets.client.AddEntityPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPacketHeadYaw extends SimplerPropertyExpression<AddEntityPacket, Float> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPacketHeadYaw.class, ExprPacketHeadYaw::new, Float.class)
                .propertyOf("[packet] head yaw", "entityspawnpackets")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public @Nullable Float convert(AddEntityPacket from) {
        return from.getHeadYaw();
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[]{Number.class};
            default -> null;
        };
    }

    @Override
    public void set(AddEntityPacket from, Float to) {
        from.setHeadYaw(to);
    }

    @Override
    public void add(AddEntityPacket from, Float to) {
        from.setHeadYaw(from.getHeadYaw() + to);
    }

    @Override
    public void remove(AddEntityPacket from, Float to) {
        add(from, to * -1F);
    }

    @Override
    protected String getPropertyName() {
        return "packet head yaw";
    }

    @Override
    public Class<? extends Float> getReturnType() {
        return Float.class;
    }
}