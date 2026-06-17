package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.patterns.MatchResult;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.RelativeFlag;
import it.jakegblp.lusk.skript.api.entry.PatternEntryData;
import it.jakegblp.lusk.skript.api.syntax.expression.DefaultValueExpression;
import it.jakegblp.lusk.skript.api.syntax.expression.ExpressionResult;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class ExprSecPlayerPositionPacket extends SectionExpression<PlayerPositionPacket> {

    public static final EntryValidator VALIDATOR;
    public static final boolean revamped = NMS.getVersion().isGreaterOrEqual(Version.of(1, 21, 2));

    static {
        var builder = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData("teleport id", new DefaultValueExpression(Integer.class, NMSApi::generateRandomTeleportId), true, Integer.class))
                .addEntryData(new PositionEntryData<>("position", true, Vector.class))
                .addEntryData(new PositionEntryData<>("x", true, Double.class))
                .addEntryData(new PositionEntryData<>("y", true, Double.class))
                .addEntryData(new PositionEntryData<>("z", true, Double.class))
                .addEntryData(new PositionEntryData<>("yaw", true, Float.class))
                .addEntryData(new PositionEntryData<>("pitch", true, Float.class));
        if (revamped)
            builder.addEntryData(new PositionEntryData<>("velocity", true, Vector.class))
                    .addEntryData(new PositionEntryData<>("x velocity", true, Double.class))
                    .addEntryData(new PositionEntryData<>("y velocity", true, Double.class))
                    .addEntryData(new PositionEntryData<>("z velocity", true, Double.class));
        VALIDATOR = builder.build();
    }

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprSecPlayerPositionPacket.class, ExprSecPlayerPositionPacket::new, PlayerPositionPacket.class, "[a] new player position packet");
    }

    public static final class PositionEntryData<T> extends PatternEntryData<T> {

        public PositionEntryData(String key, boolean optional, Class<T> returnType) {
            super(key, "(:absolute|relative) " + key, optional, returnType);
        }

        @Override
        protected @NotNull ExpressionResult<T> getValue(String value) {
            var result = super.getValue(value);
            return new PositionExpressionResult<>(result.getValue(), result.getMatchResult());
        }
    }

    public static final class PositionExpressionResult<T> extends ExpressionResult<T> {

        public PositionExpressionResult(@Nullable Expression<T> value, @NotNull MatchResult matchResult) {
            super(value, matchResult);
        }

        public boolean isRelative() {
            return !isAbsolute();
        }

        public boolean isAbsolute() {
            return hasTag("absolute");
        }
    }

    private Expression<Integer> teleportIdExpression;
    private final Set<RelativeFlag> relativeFlags = new HashSet<>();
    private PositionExpressionResult<Vector> positionExpression, velocityExpression;
    private PositionExpressionResult<Float> yawExpression, pitchExpression;
    private PositionExpressionResult<Double> xExpression, yExpression, zExpression, xVelocityExpression, yVelocityExpression, zVelocityExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        teleportIdExpression = (Expression<Integer>) container.getOptional("teleport id", false);
        yawExpression = (PositionExpressionResult<Float>) container.getOptional("yaw", false);
        pitchExpression = (PositionExpressionResult<Float>) container.getOptional("pitch", false);
        positionExpression = (PositionExpressionResult<Vector>) container.getOptional("position", false);
        xExpression = (PositionExpressionResult<Double>) container.getOptional("x", false);
        yExpression = (PositionExpressionResult<Double>) container.getOptional("y", false);
        zExpression = (PositionExpressionResult<Double>) container.getOptional("z", false);

        if (positionExpression != null && (xExpression != null || yExpression != null || zExpression != null)) {
            Skript.error("You must either provide a position or individual coordinates, not both.");
            return false;
        }

        if (revamped) {
            velocityExpression = (PositionExpressionResult<Vector>) container.getOptional("velocity", false);
            xVelocityExpression = (PositionExpressionResult<Double>) container.getOptional("x velocity", false);
            yVelocityExpression = (PositionExpressionResult<Double>) container.getOptional("y velocity", false);
            zVelocityExpression = (PositionExpressionResult<Double>) container.getOptional("z velocity", false);

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
        Float yaw, pitch;
        if (yawExpression != null) {
            yaw = yawExpression.getSingle(event);
            if (yawExpression.isRelative() || yaw == null) relativeFlags.add(RelativeFlag.Y_ROT);
            if (yaw == null) yaw = 0f;
        } else {
            yaw = 0f;
            relativeFlags.add(RelativeFlag.Y_ROT);
        }
        if (pitchExpression != null) {
            pitch = pitchExpression.getSingle(event);
            if (pitchExpression.isRelative() || pitch == null) relativeFlags.add(RelativeFlag.X_ROT);
            if (pitch == null) pitch = 0f;
        } else {
            pitch = 0f;
            relativeFlags.add(RelativeFlag.X_ROT);
        }
        Vector position = null, velocity = null;
        
        if (positionExpression != null) {
            position = positionExpression.getSingle(event);
            if (position != null && positionExpression.isRelative()) relativeFlags.addAll(RelativeFlag.getXYZ());
        }

        if (position == null) {
            Double px = xExpression != null ? xExpression.getSingle(event) : null;
            Double py = yExpression != null ? yExpression.getSingle(event) : null;
            Double pz = zExpression != null ? zExpression.getSingle(event) : null;
            if (px != null || py != null || pz != null) {
                position = new Vector(px != null ? px : 0, py != null ? py : 0, pz != null ? pz : 0);
                if (px == null || (xExpression != null && xExpression.isRelative())) relativeFlags.add(RelativeFlag.X);
                if (py == null || (yExpression != null && yExpression.isRelative())) relativeFlags.add(RelativeFlag.Y);
                if (pz == null || (zExpression != null && zExpression.isRelative())) relativeFlags.add(RelativeFlag.Z);
            } else {
                relativeFlags.addAll(RelativeFlag.getXYZ());
                position = new Vector();
            }
        }

        if (!revamped) {
            relativeFlags.addAll(RelativeFlag.getDelta());
            return new PlayerPositionPacket[]{new PlayerPositionPacket(teleportId, position, yaw, pitch, relativeFlags)};
        } else {
            Vector full = (velocityExpression != null) ? velocityExpression.getSingle(event) : null;
            if (full != null) {
                velocity = full;
                if (velocityExpression.isRelative()) relativeFlags.addAll(RelativeFlag.getDelta());
            } else {
                Double vx = (xVelocityExpression != null) ? xVelocityExpression.getSingle(event) : null;
                Double vy = (yVelocityExpression != null) ? yVelocityExpression.getSingle(event) : null;
                Double vz = (zVelocityExpression != null) ? zVelocityExpression.getSingle(event) : null;
                if (vx != null || vy != null || vz != null) {
                    velocity = new Vector(vx != null ? vx : 0, vy != null ? vy : 0, vz != null ? vz : 0);
                    if (vx == null || (xVelocityExpression != null && xVelocityExpression.isRelative())) relativeFlags.add(RelativeFlag.DELTA_X);
                    if (vy == null || (yVelocityExpression != null && yVelocityExpression.isRelative())) relativeFlags.add(RelativeFlag.DELTA_Y);
                    if (vz == null || (zVelocityExpression != null && zVelocityExpression.isRelative())) relativeFlags.add(RelativeFlag.DELTA_Z);
                }
            }
            if (velocity == null) return new PlayerPositionPacket[0];
            return new PlayerPositionPacket[]{new PlayerPositionPacket(teleportId, position, yaw, pitch, velocity, relativeFlags)};
        }
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
