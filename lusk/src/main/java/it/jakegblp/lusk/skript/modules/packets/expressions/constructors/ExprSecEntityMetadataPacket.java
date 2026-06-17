package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.modules.packets.expressions.ExprSecEntityMetadata;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;
import java.util.Map;

import static it.jakegblp.lusk.skript.modules.packets.expressions.ExprSecEntityMetadata.getMetadata;
import static it.jakegblp.lusk.skript.modules.packets.expressions.ExprSecEntityMetadata.getMetadataExpressionMap;

public class ExprSecEntityMetadataPacket extends SectionExpression<EntityMetadataPacket> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprSecEntityMetadataPacket.class, ExprSecEntityMetadataPacket::new, EntityMetadataPacket.class,
                "[a] new %entitytype% metadata packet for %protocolentityreference%");
    }

    private Expression<ProtocolEntityReference> entityReferenceExpression;
    private Expression<EntityType> entityTypeExpression;
    private Map<MetadataKey<? extends Entity, ?>, Expression<?>> expressionMap;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = ExprSecEntityMetadata.VALIDATOR.validate(node);
        if (container == null) return false;
        entityTypeExpression = (Expression<EntityType>) expressions[0];
        entityReferenceExpression = (Expression<ProtocolEntityReference>) expressions[1];
        expressionMap = getMetadataExpressionMap(entityTypeExpression, container);
        return expressionMap != null;
    }

    @Override
    protected EntityMetadataPacket[] get(Event event) {
        EntityType entityType = entityTypeExpression.getSingle(event);
        if (entityType == null) return new EntityMetadataPacket[0];
        Class<? extends Entity> type = entityType.data.getType();
        if (type == null) return new EntityMetadataPacket[0];
        ProtocolEntityReference entityReference = entityReferenceExpression.getSingle(event);
        if (entityReference == null) return new EntityMetadataPacket[0];
        return new EntityMetadataPacket[]{new EntityMetadataPacket(
                entityReference.getId(),
                entityType.data.getType(),
                getMetadata(event, type, expressionMap)
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
