package it.jakegblp.lusk.skript.elements.expressions.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.AddEntityPacket;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.UUID;

import static it.jakegblp.lusk.skript.utils.Utils.getExpressionValue;

public class ExprSecAddEntityPacket extends SectionExpression<AddEntityPacket> {

    private static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("id", null, true, Integer.class))
                .addEntryData(new ExpressionEntryData<>("uuid", null, true, UUID.class))
                .addEntryData(new ExpressionEntryData<>("velocity", null, true, Vector.class))
                .addEntryData(new ExpressionEntryData<>("data", null, true, Integer.class))
                .addEntryData(new ExpressionEntryData<>("head yaw", null, true, Number.class))
                .addEntryData(new ExpressionEntryData<>("location", null, false, Location.class, Vector.class))
                .addEntryData(new ExpressionEntryData<>("entity type", null, false, EntityType.class))
                .build();
        Skript.registerExpression(ExprSecAddEntityPacket.class, AddEntityPacket.class, ExpressionType.SIMPLE,
                "[a] new (entity (spawn|add)|add entity) packet");
    }

    private Expression<Integer> entityIdExpression;
    private Expression<UUID> uuidExpression;
    private Expression<Object> locationExpression;
    private Expression<EntityType> entityTypeExpression;
    private Expression<Vector> velocityExpression;
    private Expression<Number> dataExpression;
    private Expression<Number> headYawExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        entityIdExpression = (Expression<Integer>) container.getOptional("id", false);
        uuidExpression = (Expression<UUID>) container.getOptional("uuid", false);
        locationExpression = (Expression<Object>) container.get("location", false);
        entityTypeExpression = (Expression<EntityType>) container.get("entity type", false);
        velocityExpression = (Expression<Vector>) container.getOptional("velocity", false);
        dataExpression = (Expression<Number>) container.getOptional("data", false);
        headYawExpression = (Expression<Number>) container.getOptional("head yaw", false);
        return true;
    }

    @Override
    protected AddEntityPacket[] get(Event event) {
        Object unprocessedLocation = locationExpression.getSingle(event);
        double x,y,z;
        float yaw, pitch;
        if (unprocessedLocation instanceof Location location) {
            x = location.x();
            y = location.y();
            z = location.z();
            yaw = location.getYaw();
            pitch = location.getPitch();
        } else if (unprocessedLocation instanceof Vector vector) {
            x = vector.getX();
            y = vector.getY();
            z = vector.getZ();
            yaw = 0;
            pitch = 0;
        }
        else return new AddEntityPacket[0];
        EntityType type = entityTypeExpression.getSingle(event);
        if (type == null) return new AddEntityPacket[0];
        return new AddEntityPacket[]{new AddEntityPacket(
                getExpressionValue(entityIdExpression, event, NMSApi.generateRandomEntityId()),
                getExpressionValue(uuidExpression, event, UUID.randomUUID()),
                x,
                y,
                z,
                yaw,
                pitch,
                EntityUtils.toBukkitEntityType(type.data),
                getExpressionValue(dataExpression, event, 0).intValue(),
                getExpressionValue(velocityExpression, event, new Vector()),
                getExpressionValue(headYawExpression, event, 0).doubleValue()
        )};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AddEntityPacket> getReturnType() {
        return AddEntityPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new entity spawn packet";
    }
}
