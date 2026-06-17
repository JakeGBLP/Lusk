package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.entity.EntityType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprEntityMetadataPacket extends SimpleExpression<EntityMetadataPacket> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprEntityMetadataPacket.class, ExprEntityMetadataPacket::new, EntityMetadataPacket.class,
                "[a] new %entitytype% metadata packet for %protocolentityreference% with %entitymetadata%");
    }

    private Expression<ProtocolEntityReference> entityReferenceExpression;
    private Expression<EntityType> entityTypeExpression;
    private Expression<EntityMetadata> entityMetadataExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityTypeExpression = (Expression<EntityType>) expressions[0];
        entityReferenceExpression = (Expression<ProtocolEntityReference>) expressions[1];
        entityMetadataExpression = (Expression<EntityMetadata>) expressions[2];
        return true;
    }

    @Override
    protected EntityMetadataPacket[] get(Event event) {
        EntityType entityType = entityTypeExpression.getSingle(event);
        if (entityType == null) return new EntityMetadataPacket[0];
        ProtocolEntityReference entityReference = entityReferenceExpression.getSingle(event);
        if (entityReference == null) return new EntityMetadataPacket[0];
        EntityMetadata metadata = entityMetadataExpression.getSingle(event);
        if (metadata == null) return new EntityMetadataPacket[0];
        return new EntityMetadataPacket[]{new EntityMetadataPacket(
                entityReference.getId(),
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
        return "a new " + entityTypeExpression.toString(event, debug) + " spawn packet " + entityReferenceExpression.toString(event, debug) + " with " + entityMetadataExpression.toString(event, debug);
    }

}
