package it.jakegblp.lusk.skript.modules.packets.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import it.jakegblp.lusk.nms.core.world.entity.metadata.news.OptionalEntityDataKey;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.NMSSyntax;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.*;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Setter
public class ExprSecEntityMetadata extends SectionExpression<EntityMetadata> implements NMSSyntax {

    public static final EntryValidator VALIDATOR;

    static {
        var builder = EntryValidator.builder();
        for (var entry : NMS.getMetadataKeyRegistry().metadataEntries().values()) {
            String name = entry.getName().toLowerCase(Locale.ROOT);
            if (!name.contains("unused")) {
                //Bukkit.getLogger().info("lusk: registered metadata key: " + name);
                builder.addEntryData(new ExpressionEntryData<>(name.replace("_", " "), null, true, entry.getPrettyType()));
            }
        }
        VALIDATOR = builder.build();
    }

    private Expression<EntityType> entityTypeExpression;
    private Map<EntityDataKey<? extends Entity, ?, ?, ?, ?>, Expression<?>> expressionMap;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprSecEntityMetadata.class, ExprSecEntityMetadata::new, EntityMetadata.class)
                .patterns("[a] new %entitytype% metadata [object]")
                .register(syntaxRegistry);
    }

    public static Map<EntityDataKey<? extends Entity, ?, ?, ?, ?>, Expression<?>> getMetadataExpressionMap(
            Expression<EntityType> entityTypeExpression,
            EntryContainer container
    ) {
        Map<EntityDataKey<? extends Entity, ?, ?, ?, ?>, Expression<?>> expressionMap = new HashMap<>();
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

        var metadataKeyRegistry = NMS.getMetadataKeyRegistry();
        for (var entry : metadataKeyRegistry.metadataEntries().asMap().entrySet()) {
            var entityClasses = entry.getKey();
            var namedMetadataKeyList = entry.getValue();
            for (var key : namedMetadataKeyList) {
                String name = key.getName();
                String normalizedName = name.toLowerCase().replace("_", " ");
                if (container.hasEntry(normalizedName)) {
                    if ((type == null || entityClasses.isAssignableFrom(type)) && unsupportedProperties.isEmpty())
                        expressionMap.put(key, (Expression<?>) container.getOptional(normalizedName, false));
                    else
                        unsupportedProperties.add(name);
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

    public static EntityMetadata getMetadata(Event event, Class<? extends Entity> type, Map<EntityDataKey<? extends Entity, ?, ?, ?, ?>, Expression<?>> expressionMap) {
        EntityMetadata entityMetadata = new EntityMetadata();
        for (var entry : expressionMap.entrySet()) {
            EntityDataKey<? extends Entity, ?, ?, ?, ?> key = entry.getKey();
            if (key.getEntityClasses().isAssignableFrom(type)) {
                Object value = entry.getValue().getSingle(event);
                if (key instanceof OptionalEntityDataKey)
                    value = Optional.ofNullable(value);
                else if (value == null) {
                    Skript.error("A value has to be passed for the following key: " + Classes.toString(key));
                    continue;
                }
                if (!entityMetadata.setUnsafe(key, value)) {
                    Skript.error("Could not set key " + Classes.toString(key) + " to value " + Classes.toString(value));
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
