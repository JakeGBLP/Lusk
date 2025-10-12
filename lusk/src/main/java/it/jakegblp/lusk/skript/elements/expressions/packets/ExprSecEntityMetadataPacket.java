package it.jakegblp.lusk.skript.elements.expressions.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.skript.api.DynamicEntryData;
import it.jakegblp.lusk.skript.elements.expressions.ExprSecEntityMetadata;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.Map;

public class ExprSecEntityMetadataPacket extends SectionExpression<EntityMetadataPacket> {

    private static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("id", null, false, Integer.class))
                .addEntryData(new DynamicEntryData("metadata", EntityMetadata.class, ExprSecEntityMetadata.VALIDATOR))
                .build();
        Skript.registerExpression(ExprSecEntityMetadataPacket.class, EntityMetadataPacket.class, ExpressionType.SIMPLE,
                "[a] new %entitytype% metadata packet");
    }

    private Expression<Number> idExpression;
    private Expression<EntityType> entityTypeExpression;
    private Map<MetadataKey<? extends Entity, ?>, Expression<?>> expressionMap;
    private Expression<EntityMetadata> entityMetadataExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        entityTypeExpression = (Expression<EntityType>) expressions[0];
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        idExpression = (Expression<Number>) container.get("id", false);
        Object metadata = container.getOptional("metadata", false);
        if (metadata instanceof EntryContainer entryContainer)
            expressionMap = ExprSecEntityMetadata.getMetadataExpressionMap(entityTypeExpression, entryContainer);
        else
            entityMetadataExpression = (Expression<EntityMetadata>) metadata;
        return true;
    }

    @Override
    protected EntityMetadataPacket[] get(Event event) {
        EntityType entityType = entityTypeExpression.getSingle(event);
        if (entityType == null) return new EntityMetadataPacket[0];
        Number id = idExpression.getSingle(event);
        if (id == null) return new EntityMetadataPacket[0];
        EntityMetadata metadata;
        if (expressionMap != null) {
            metadata = ExprSecEntityMetadata.getMetadata(event, entityType.data.getType(), expressionMap);
        } else {
            metadata = entityMetadataExpression.getSingle(event);
        }
        if (metadata == null) return new EntityMetadataPacket[0];
        return new EntityMetadataPacket[]{new EntityMetadataPacket(
                id.intValue(),
                entityType.data.getType(),
                metadata
        )};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends EntityMetadataPacket> getReturnType() {
        return EntityMetadataPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new entity spawn packet";
    }
}
