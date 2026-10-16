package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithPosition;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.bukkit.Axis;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Locale;

public class ExprPositionPacketComponents extends SimplerPropertyExpression<PacketWithPosition, Object> {

    private Axis axis;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPositionPacketComponents.class, ExprPositionPacketComponents::new, Object.class)
                .propertyOf("packet ((:x|:y|:z)[( |-)(coord[inate]|pos[ition]|loc[ation])]|position) [component]", "genericpositionpacket")
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
    public @Nullable Object convert(PacketWithPosition from) {
        return switch (axis) {
            case X -> from.getPositionX();
            case Y -> from.getPositionY();
            case Z -> from.getPositionZ();
            case null -> from.getPosition();
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
    public void set(PacketWithPosition from, Object to) {
        switch (axis) {
            case X -> from.setPositionX((Double) to);
            case Y -> from.setPositionY((Double) to);
            case Z -> from.setPositionZ((Double) to);
            case null -> from.setPosition((Vector) to);
        }
    }

    @Override
    public void add(PacketWithPosition from, Object to) {
        switch (axis) {
            case X -> from.setPositionX(from.getPositionX() + (Double) to);
            case Y -> from.setPositionY(from.getPositionY() + (Double) to);
            case Z -> from.setPositionZ(from.getPositionZ() + (Double) to);
            case null -> from.setPosition(from.getPosition().add((Vector) to));
        }
    }

    @Override
    public void remove(PacketWithPosition from, Object to) {
        add(from, axis == null ? ((Vector) to).multiply(-1) : ((Double) to) * -1);
    }

    @Override
    public void delete(PacketWithPosition from) {
        set(from, axis == null ? new Vector() : 0d);
    }

    @Override
    protected String getPropertyName() {
        return "packet " + (axis == null ? "position" : (axis.name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public Class<?> getReturnType() {
        return axis == null ? Vector.class : Number.class;
    }
}