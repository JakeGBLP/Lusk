package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPacketTeleportId extends SimplerPropertyExpression<PlayerPositionPacket, Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPacketTeleportId.class, ExprPacketTeleportId::new, Integer.class)
                .propertyOf("[packet] teleport id", "playerpositionpackets")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public @Nullable Integer convert(PlayerPositionPacket from) {
        return from.getTeleportId();
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[]{Integer.class};
            default -> null;
        };
    }

    @Override
    public void set(PlayerPositionPacket from, Integer to) {
        from.setTeleportId(to);
    }

    @Override
    public void add(PlayerPositionPacket from, Integer to) {
        from.setTeleportId(from.getTeleportId() + to);
    }

    @Override
    public void remove(PlayerPositionPacket from, Integer to) {
        add(from, to * -1);
    }

    @Override
    protected String getPropertyName() {
        return "packet teleport id";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}