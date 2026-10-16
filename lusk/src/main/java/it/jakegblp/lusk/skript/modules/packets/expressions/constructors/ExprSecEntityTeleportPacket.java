package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityTeleportPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

public class ExprSecEntityTeleportPacket extends SectionExpression<EntityTeleportPacket> {

    public static final EntryValidator VALIDATOR = EntryValidator.builder()
            .addEntryData(new ExpressionEntryData<>("entity id", null, false, Integer.class))
            .addEntryData(new ExpressionEntryData<>("location", null, false, Location.class))
            .addEntryData(new ExpressionEntryData<>("velocity", null, false, Vector.class))
            .addEntryData(new ExpressionEntryData<>("on ground", null, false, Boolean.class))
            .build();

    private Expression<Integer> entityIdExpression;
    private Expression<Location> locationExpression;
    private Expression<Vector> velocityExpression;
    private Expression<Boolean> onGroundExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprSecEntityTeleportPacket.class, ExprSecEntityTeleportPacket::new, EntityTeleportPacket.class)
                .patterns("[a] new [entity] teleport packet")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        entityIdExpression = (Expression<Integer>) container.getOptional("entity id", false);
        locationExpression = (Expression<Location>) container.getOptional("location", false);
        velocityExpression = (Expression<Vector>) container.getOptional("velocity", false);
        onGroundExpression = (Expression<Boolean>) container.getOptional("on ground", false);
        return entityIdExpression != null && locationExpression != null && velocityExpression != null && onGroundExpression != null;
    }

    @Override
    protected EntityTeleportPacket @Nullable [] get(Event event) {
        Integer entityId = entityIdExpression.getSingle(event);
        if (entityId == null) return new EntityTeleportPacket[0];
        Location location = locationExpression.getSingle(event);
        if (location == null) return new EntityTeleportPacket[0];
        Vector velocity = velocityExpression.getSingle(event);
        if (velocity == null) return new EntityTeleportPacket[0];
        Boolean onGround = onGroundExpression.getSingle(event);
        if (onGround == null) return new EntityTeleportPacket[0];
        return new EntityTeleportPacket[]{new EntityTeleportPacket(entityId, location, velocity, onGround)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends EntityTeleportPacket> getReturnType() {
        return EntityTeleportPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "new entity teleport packet";
    }

}
