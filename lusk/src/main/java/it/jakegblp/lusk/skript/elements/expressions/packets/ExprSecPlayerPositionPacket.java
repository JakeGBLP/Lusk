package it.jakegblp.lusk.skript.elements.expressions.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.nms.core.world.entity.flags.entity.RelativeFlag;
import it.jakegblp.lusk.skript.api.DefaultValueExpression;
import it.jakegblp.lusk.skript.api.DoubleKeyExpressionEntryData;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExprSecPlayerPositionPacket extends SectionExpression<PlayerPositionPacket> {

    public static final EntryValidator VALIDATOR;

    static {
        var builder = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("teleport id", new DefaultValueExpression<>(Integer.class, NMSApi::generateRandomTeleportId), true, Integer.class))
                .addEntryData(new DoubleKeyExpressionEntryData("relative position", "absolute position", null, true, Vector.class, Location.class))
                .addEntryData(new DoubleKeyExpressionEntryData<>("relative x", "absolute x", null, true, Double.class))
                .addEntryData(new DoubleKeyExpressionEntryData<>("relative y", "absolute y", null, true, Double.class))
                .addEntryData(new DoubleKeyExpressionEntryData<>("relative z", "absolute z", null, true, Double.class))
                .addEntryData(new DoubleKeyExpressionEntryData<>("relative yaw", "absolute yaw", null, true, Float.class))
                .addEntryData(new DoubleKeyExpressionEntryData<>("relative pitch", "absolute pitch", null, true, Float.class));
        if (RelativeFlag.isRevamped())
            builder.addEntryData(new DoubleKeyExpressionEntryData<>("relative velocity", "absolute velocity", null, true, Vector.class))
                    .addEntryData(new DoubleKeyExpressionEntryData<>("relative x velocity", "absolute x velocity", null, true, Double.class))
                    .addEntryData(new DoubleKeyExpressionEntryData<>("relative y velocity", "absolute y velocity", null, true, Double.class))
                    .addEntryData(new DoubleKeyExpressionEntryData<>("relative z velocity", "absolute z velocity", null, true, Double.class));

        VALIDATOR = builder.build();
        Skript.registerExpression(ExprSecPlayerPositionPacket.class, PlayerPositionPacket.class, ExpressionType.COMBINED,
                "new player position packet");
    }

    private Expression<Integer> teleportIdExpression;
    private final Set<RelativeFlag> relativeFlags = new HashSet<>();
    private Expression<Object> positionExpression;
    private Expression<Vector> velocityExpression;
    private Expression<Float> yawExpression, pitchExpression;
    private Expression<Double> xExpression, yExpression, zExpression, xVelocityExpression, yVelocityExpression, zVelocityExpression;

    @Nullable
    public <T> Expression<T> relativeOrAbsolute(EntryContainer container, String name, RelativeFlag... relativeFlags) {
        return relativeOrAbsolute(container, name, Arrays.asList(relativeFlags));
    }
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> Expression<T> relativeOrAbsolute(EntryContainer container, String name, List<RelativeFlag> relativeFlags) {
        String relative = "relative " + name;
        if (container.hasEntry(relative)) {
            this.relativeFlags.addAll(relativeFlags);
            return (Expression<T>) container.getOptional(relative, false);
        }
        String absolute = "absolute " + name;
        if (container.hasEntry(absolute)) {
            relativeFlags.forEach(this.relativeFlags::remove);
            return (Expression<T>) container.getOptional(absolute, false);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        teleportIdExpression = (Expression<Integer>) container.getOptional("teleport id", false);
        yawExpression = relativeOrAbsolute(container, "yaw", RelativeFlag.Y_ROT);
        pitchExpression = relativeOrAbsolute(container, "pitch", RelativeFlag.X_ROT);
        positionExpression = relativeOrAbsolute(container, "position", RelativeFlag.getXYZ());
        xExpression = relativeOrAbsolute(container, "x", RelativeFlag.X);
        yExpression = relativeOrAbsolute(container, "y", RelativeFlag.Y);
        zExpression = relativeOrAbsolute(container, "z", RelativeFlag.Z);

        if (positionExpression != null && (xExpression != null || yExpression != null || zExpression != null)) {
            Skript.error("You must either provide a position or individual coordinates, not both.");
            return false;
        }

        if (RelativeFlag.isRevamped()) {
            velocityExpression = relativeOrAbsolute(container, "velocity", RelativeFlag.getDelta());
            xVelocityExpression = relativeOrAbsolute(container, "x velocity", RelativeFlag.DELTA_X);
            yVelocityExpression = relativeOrAbsolute(container, "y velocity", RelativeFlag.DELTA_Y);
            zVelocityExpression = relativeOrAbsolute(container, "z velocity", RelativeFlag.DELTA_Z);

            if (velocityExpression != null && (xVelocityExpression != null || yVelocityExpression != null || zVelocityExpression != null)) {
                Skript.error("You must either provide a velocity or individual velocity coordinates, not both.");
                return false;
            }
        }
        return true;
    }

    @Override
    protected PlayerPositionPacket @Nullable [] get(Event event) {
        Integer teleportId = teleportIdExpression.getSingle(event);
        if (teleportId == null) return new PlayerPositionPacket[0];
        Float yaw = null, pitch = null;
        if (yawExpression != null)
            yaw = yawExpression.getSingle(event);
        if (yawExpression == null || yaw == null) {
            relativeFlags.add(RelativeFlag.Y_ROT);
            yaw = 0f;
        }
        if (pitchExpression != null)
            pitch = pitchExpression.getSingle(event);
        if (pitchExpression == null || pitch == null) {
            relativeFlags.add(RelativeFlag.X_ROT);
            pitch = 0f;
        }
        Vector position = null, velocity = null;

        if (positionExpression != null) {
            Object obj = positionExpression.getSingle(event);
            if (obj instanceof Vector v) {
                position = v;
            } else if (obj instanceof Location loc) {
                position = loc.toVector();
            }
        }

        if (position == null) {
            Double px = xExpression != null ? xExpression.getSingle(event) : null;
            Double py = yExpression != null ? yExpression.getSingle(event) : null;
            Double pz = zExpression != null ? zExpression.getSingle(event) : null;
            if (px != null || py != null || pz != null) {
                Vector combined = new Vector();
                if (px != null) combined.setX(px);
                else relativeFlags.add(RelativeFlag.X);
                if (py != null) combined.setY(py);
                else relativeFlags.add(RelativeFlag.Y);
                if (pz != null) combined.setZ(pz);
                else relativeFlags.add(RelativeFlag.Z);
                position = combined;
            } else {
                relativeFlags.addAll(RelativeFlag.getXYZ());
                position = new Vector();
            }
        }

        if (!RelativeFlag.isRevamped())
            relativeFlags.addAll(RelativeFlag.getDelta());
        else {
            Vector full = (velocityExpression != null) ? velocityExpression.getSingle(event) : null;
            if (full != null)
                velocity = full;
            else {
                Double vx = (xVelocityExpression != null) ? xVelocityExpression.getSingle(event) : null;
                Double vy = (yVelocityExpression != null) ? yVelocityExpression.getSingle(event) : null;
                Double vz = (zVelocityExpression != null) ? zVelocityExpression.getSingle(event) : null;
                if (vx != null || vy != null || vz != null) {
                    Vector combined = new Vector();
                    if (vx != null) combined.setX(vx);
                    else relativeFlags.add(RelativeFlag.DELTA_X);
                    if (vy != null) combined.setY(vy);
                    else relativeFlags.add(RelativeFlag.DELTA_Y);
                    if (vz != null) combined.setZ(vz);
                    else relativeFlags.add(RelativeFlag.DELTA_Z);
                    velocity = combined;
                }
            }
        }
        return new PlayerPositionPacket[]{new PlayerPositionPacket(teleportId, position, velocity, yaw, pitch, relativeFlags)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PlayerPositionPacket> getReturnType() {
        return PlayerPositionPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "new player position packet";
    }
}
