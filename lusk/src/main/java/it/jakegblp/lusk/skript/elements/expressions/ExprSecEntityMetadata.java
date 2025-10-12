package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.*;

import static it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys.NAMED_KEYS;

@Setter
public class ExprSecEntityMetadata extends SectionExpression<EntityMetadata> {

    public static final EntryValidator VALIDATOR;

    static {
        var builder = EntryValidator.builder();
        for (var entry : NAMED_KEYS.entrySet()) {
            String name = entry.getKey().toLowerCase(Locale.ROOT);
            if (!name.contains("unused")) {
                builder.addEntryData(new ExpressionEntryData<>(name.replace("_", " "), null, true, entry.getValue().valueClass()));
            }
        }
        VALIDATOR = builder.build();
        Skript.registerExpression(ExprSecEntityMetadata.class, EntityMetadata.class, ExpressionType.COMBINED,
                "[a] new %entitytype% metadata [object]");
    }

    private Expression<EntityType> entityTypeExpression;
    private Map<MetadataKey<? extends Entity, ?>, Expression<?>> expressionMap;

    public static Map<MetadataKey<? extends Entity, ?>, Expression<?>> getMetadataExpressionMap(
            Expression<EntityType> entityTypeExpression,
            EntryContainer container
    ) {
        Map<MetadataKey<? extends Entity, ?>, Expression<?>> expressionMap = new HashMap<>();
        List<String> unsupportedProperties = new ArrayList<>();

        Class<? extends Entity> type = null;
        EntityType entityType = null;
        if (entityTypeExpression instanceof Literal<EntityType> entityTypeLiteral) {
            entityType = entityTypeLiteral.getSingle();
            if (entityType == null)
                return null;
            type = entityType.data.getType();
            if (type == null)
                return null;
        }

        var inverseNameMap = NAMED_KEYS.inverse();
        for (var entry : MetadataKeys.METADATA_ENTRIES.asMap().entrySet()) {
            Class<?> supportedClass = entry.getKey();
            var namedMetadataKeyList = entry.getValue();
            for (var key : namedMetadataKeyList) {
                String name = inverseNameMap.get(key);
                String normalizedName = name.toLowerCase().replace("_", " ");
                if (container.hasEntry(normalizedName)) {
                    if (type == null || supportedClass.isAssignableFrom(type)) {
                        if (unsupportedProperties.isEmpty()) {
                            expressionMap.put(key, (Expression<?>) container.getOptional(normalizedName, false));
                        }
                    } else {
                        unsupportedProperties.add(name);
                    }
                }
            }
        }
        if (!unsupportedProperties.isEmpty()) {
            String errorMessage = entityType + " does not support the following properties: " + String.join(", ", unsupportedProperties);
            Skript.error(errorMessage);
            return null;
        }

        return expressionMap;
    }

    public static EntityMetadata getMetadata(Event event, Class<? extends Entity> type, Map<MetadataKey<? extends Entity, ?>, Expression<?>> expressionMap) {
        EntityMetadata entityMetadata = new EntityMetadata();
        for (var entry : expressionMap.entrySet()) {
            MetadataKey<? extends Entity, ?> key = entry.getKey();
            Class<? extends Entity> entityClass = key.entityClass();
            if (entityClass.isAssignableFrom(type)) {
                Object value = entry.getValue().getSingle(event);
                if (value != null) {
                    System.out.println("key: "+key+", value: "+value);
                    if (!entityMetadata.setUnsafe(key, value)) {
                        Skript.error("Could not set key "+ Classes.toString(key) + " to value " + Classes.toString(value));
                    }
                }
            }
        }
        return entityMetadata;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        entityTypeExpression = (Expression<EntityType>) expressions[0];
        expressionMap = getMetadataExpressionMap(entityTypeExpression, container);
        return expressionMap != null;
    }

    @Override
    protected EntityMetadata[] get(Event event) {
        EntityType entityType = entityTypeExpression.getSingle(event);
        if (entityType == null) return new EntityMetadata[0];
        Class<? extends Entity> type = entityType.data.getType();
        if (type == null) return new EntityMetadata[0];
        return new EntityMetadata[]{getMetadata(event, type, expressionMap)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends EntityMetadata> getReturnType() {
        return EntityMetadata.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new " + entityTypeExpression.toString(event, debug) + " metadata";
    }
}
