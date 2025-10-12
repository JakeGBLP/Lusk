package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.nms.core.world.entity.flags.entity.RelativeFlag;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operator;

import java.util.*;

import static ch.njol.skript.classes.Changer.ChangeMode.*;
import static it.jakegblp.lusk.nms.core.world.entity.flags.entity.RelativeFlag.*;

public class ExprPositionPacketComponents extends PropertyExpression<PlayerPositionPacket, Object> {

    static {
        register(ExprPositionPacketComponents.class, Object.class,
                RelativeFlag.isRevamped() ? "(:relative|absolute) (position:location|:position|:velocity|(:x|:y|:z) [:velocity]|(:yaw|:pitch))"
                        : "(:relative|absolute) (position:location|:position|:x|:y|:z|(:yaw|:pitch))", "playerpositionpackets");
    }

    private boolean relative, velocity, position;

    private RelativeFlag relativeFlag;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        relative = parseResult.hasTag("relative");
        velocity = parseResult.hasTag("velocity");
        position = parseResult.hasTag("position");
        if (parseResult.hasTag("x"))
            relativeFlag = velocity ? DELTA_X : X;
        else if (parseResult.hasTag("y"))
            relativeFlag = velocity ? DELTA_Y : Y;
        else if (parseResult.hasTag("z"))
            relativeFlag = velocity ? DELTA_Z : Z;
        else if (parseResult.hasTag("yaw"))
            relativeFlag = Y_ROT;
        else if (parseResult.hasTag("pitch"))
            relativeFlag = X_ROT;
        setExpr((Expression<PlayerPositionPacket>) expressions[0]);
        return true;
    }

    @Override
    protected Object[] get(Event event, PlayerPositionPacket[] source) {
        return Arrays.stream(source).map(packet -> {
            var currentFlags = packet.getRelativeFlags();
            if (position) {
                return packet.getPosition();
            } else if (velocity) {
                return packet.getVelocity();
            }
            if (relative == currentFlags.contains(relativeFlag)) {
                return switch (relativeFlag) {
                    case X -> packet.getX();
                    case Y -> packet.getY();
                    case Z -> packet.getZ();
                    case Y_ROT -> packet.getYaw();
                    case X_ROT -> packet.getPitch();
                    case DELTA_X -> packet.getVelocityX();
                    case DELTA_Y -> packet.getVelocityY();
                    case DELTA_Z -> packet.getVelocityZ();
                    default -> null;
                };
            }
            return null;
        }).filter(Objects::nonNull).toArray();
    }

    @Override
    public Class<?> getReturnType() {
        if (position || velocity) return Vector.class;
        return switch (relativeFlag) {
            case X, Y, Z, DELTA_X, DELTA_Y, DELTA_Z -> Double.class;
            case Y_ROT, X_ROT -> Float.class;
            case ROTATE_DELTA -> null;
        };
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE, DELETE -> new Class<?>[] { getReturnType() };
            default -> null;
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        Object changeValue = null;
        if (mode != DELETE) {
            if (delta == null || delta.length == 0) return;
            changeValue = delta[0];
        }
        Class<?> type = velocity || position ? Vector.class : Number.class;
        for (PlayerPositionPacket packet : getExpr().getAll(event)) {
            Object finalValue;
            if (mode == SET || mode == DELETE) {
                finalValue = changeValue;
            } else {
                Object currentValue = get(event)[0];
                finalValue = Arithmetics.calculate(mode == ADD ? Operator.ADDITION : Operator.SUBTRACTION, currentValue, changeValue, type);
            }
            var currentFlags = packet.getRelativeFlags();
            if (position) {
                if (relative)
                    currentFlags.addAll(RelativeFlag.getXYZ());
                else
                    RelativeFlag.getXYZ().forEach(currentFlags::remove);
                packet.setPosition((Vector) finalValue);
            } else if (velocity) {
                if (relative)
                    currentFlags.addAll(RelativeFlag.getDelta());
                else
                    RelativeFlag.getDelta().forEach(currentFlags::remove);
                packet.setVelocity((Vector) finalValue);
            } else {
                if (relative)
                    currentFlags.add(relativeFlag);
                else
                    currentFlags.remove(relativeFlag);
                switch (relativeFlag) {
                    case X -> packet.setX((Double) finalValue);
                    case Y -> packet.setY((Double) finalValue);
                    case Z -> packet.setZ((Double) finalValue);
                    case Y_ROT -> packet.setYaw((Float) finalValue);
                    case X_ROT -> packet.setPitch((Float) finalValue);
                    case DELTA_X -> packet.setVelocityX((Double) finalValue);
                    case DELTA_Y -> packet.setVelocityY((Double) finalValue);
                    case DELTA_Z -> packet.setVelocityZ((Double) finalValue);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        SyntaxStringBuilder sb = new SyntaxStringBuilder(event, debug);
        sb.append(relative ? "relative " : "absolute ");
        if (position) sb.append("position");
        else if (relativeFlag != null)
            sb.append(switch (relativeFlag) {
                case X -> "x";
                case Y -> "y";
                case Z -> "z";
                case DELTA_X -> "x velocity";
                case DELTA_Y -> "y velocity";
                case DELTA_Z -> "z velocity";
                case Y_ROT -> "yaw";
                case X_ROT -> "pitch";
                default -> "?";
            });
        else if (velocity) sb.append("velocity");
        else sb.append("?");
        return sb.append(" of ").append(getExpr()).toString();
    }
}
