package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithPosition;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPacketPosition extends SimplerPropertyExpression<PacketWithPosition, Vector> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPacketPosition.class, ExprPacketPosition::new, Vector.class)
                .propertyOf("packet (location|position)", "genericpositionpackets")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public @Nullable Vector convert(PacketWithPosition from) {
        return from.getPosition();
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[] {Vector.class};
            case DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void set(PacketWithPosition from, Vector to) {
        from.setPosition(to);
    }

    @Override
    public void add(PacketWithPosition from, Vector to) {
        from.setPosition(from.getPosition().add(to));
    }

    @Override
    public void remove(PacketWithPosition from, Vector to) {
        add(from, to.multiply(-1));
    }

    @Override
    protected String getPropertyName() {
        return "packet position";
    }

    @Override
    public Class<? extends Vector> getReturnType() {
        return Vector.class;
    }
}