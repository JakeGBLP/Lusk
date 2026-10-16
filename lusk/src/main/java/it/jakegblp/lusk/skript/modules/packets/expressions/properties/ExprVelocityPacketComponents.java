package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithVelocity;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.bukkit.Axis;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Locale;

public class ExprVelocityPacketComponents extends SimplerPropertyExpression<PacketWithVelocity, Object> {

    private Axis axis;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprVelocityPacketComponents.class, ExprVelocityPacketComponents::new, Object.class)
                .propertyOf("packet velocity (:x|:y|:z) [component]", "genericpositionpacket")
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
    public @Nullable Object convert(PacketWithVelocity from) {
        return switch (axis) {
            case X -> from.getVelocityX();
            case Y -> from.getVelocityY();
            case Z -> from.getVelocityZ();
            case null -> from.getVelocity();
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
    public void set(PacketWithVelocity from, Object to) {
        switch (axis) {
            case X -> from.setVelocityX((Double) to);
            case Y -> from.setVelocityY((Double) to);
            case Z -> from.setVelocityZ((Double) to);
            case null -> from.setVelocity((Vector) to);
        }
    }

    @Override
    public void add(PacketWithVelocity from, Object to) {
        switch (axis) {
            case X -> from.setVelocityX(from.getVelocityX() + (Double) to);
            case Y -> from.setVelocityY(from.getVelocityY() + (Double) to);
            case Z -> from.setVelocityZ(from.getVelocityZ() + (Double) to);
            case null -> from.setVelocity(from.getVelocity().add((Vector) to));
        }
    }

    @Override
    public void remove(PacketWithVelocity from, Object to) {
        add(from, axis == null ? ((Vector) to).multiply(-1) : ((Double) to) * -1);
    }

    @Override
    public void delete(PacketWithVelocity from) {
        set(from, axis == null ? new Vector() : 0d);
    }

    @Override
    protected String getPropertyName() {
        return "packet velocity " + (axis == null ? "" : (axis.name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public Class<?> getReturnType() {
        return axis == null ? Vector.class : Number.class;
    }
}