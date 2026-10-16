package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithMovement;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.bukkit.Axis;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Locale;

public class ExprMovementPacketComponents extends SimplerPropertyExpression<PacketWithMovement, Object> {

    private Axis axis;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprMovementPacketComponents.class, ExprMovementPacketComponents::new, Object.class)
                .propertyOf("packet movement (:x|:y|:z) [component]", "genericpositionpacket")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        var tags = parseResult.tags;
        axis = tags == null ? null : Axis.valueOf(parseResult.tags.getFirst().toUpperCase(Locale.ROOT));
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Object convert(PacketWithMovement from) {
        return switch (axis) {
            case X -> from.getMovementX();
            case Y -> from.getMovementY();
            case Z -> from.getMovementZ();
            case null -> from.getMovement();
        };
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, SET, REMOVE -> new Class[]{axis == null ? Vector.class : Double.class};
            case DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void set(PacketWithMovement from, Object to) {
        switch (axis) {
            case X -> from.setMovementX((Double) to);
            case Y -> from.setMovementY((Double) to);
            case Z -> from.setMovementZ((Double) to);
            case null -> from.setMovement((Vector) to);
        }
    }

    @Override
    public void add(PacketWithMovement from, Object to) {
        switch (axis) {
            case X -> from.setMovementX(from.getMovementX() + (Double) to);
            case Y -> from.setMovementY(from.getMovementY() + (Double) to);
            case Z -> from.setMovementZ(from.getMovementZ() + (Double) to);
            case null -> from.setMovement(from.getMovement().add((Vector) to));
        }
    }

    @Override
    public void remove(PacketWithMovement from, Object to) {
        add(from, axis == null ? ((Vector) to).multiply(-1) : ((Double) to) * -1);
    }

    @Override
    public void delete(PacketWithMovement from) {
        set(from, axis == null ? new Vector() : 0d);
    }

    @Override
    protected String getPropertyName() {
        return "packet movement " + (axis == null ? "" : (axis.name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public Class<?> getReturnType() {
        return axis == null ? Vector.class : Number.class;
    }
}