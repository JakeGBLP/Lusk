package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.AddEntityPacket;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.OptionallySectionEffect;
import it.jakegblp.lusk.skript.elements.expressions.ExprSecEntityMetadata;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static it.jakegblp.lusk.skript.utils.Utils.getSingleDefaultOrNull;

@Name("[NMS] Player - Spawn Fake Entity")
@Description("""
        Spawns a client sided (fake) entity for the provided players.
        
        If you don't put in an ID, one will be provided for you, randomly.
        The entity's UUID is random.
        
        If you wish to modify the uuid, entity data, velocity or head rotation (1.19+), use the spawn packet section expression.
        """)
@Examples({
        "spawn fake zombie at player to player"
})
@Keywords({
        "packets", "packet", "protocol", "fake", "clientsided", "clientside"
})
@Since("2.0.0")
public class EffSecSpawnFakeEntity extends OptionallySectionEffect {

    static {
        Skript.registerSection(EffSecSpawnFakeEntity.class, "spawn [a] (fake|client[-| ]sided) %entitytype% [with id %-number%] %directions% %location% for %players%");
    }

    private Expression<EntityType> entityTypeExpression;
    private Expression<Number> idExpression;
    private Expression<Location> locationExpression;
    private Expression<Player> playersExpression;
    private Map<MetadataKey<? extends Entity, ?>, Expression<?>> expressionMap;

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "spawn fake " + entityTypeExpression.toString(event, b) + (idExpression != null ? " with id " + idExpression.toString(event, b) : "") + " " + locationExpression.toString(event, b) + (this.hasSection() ? " with metadata" : "");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        entityTypeExpression = (Expression<EntityType>) expressions[0];
        idExpression = (Expression<Number>) expressions[1];
        locationExpression = Direction.combine((Expression<? extends Direction>) expressions[2], (Expression<? extends Location>) expressions[3]);
        playersExpression = (Expression<Player>) expressions[4];
        return super.init(expressions, pattern, delayed, result, node, triggerItems);
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        Location loc = locationExpression.getSingle(event);
        if (loc != null) {
            EntityType type = entityTypeExpression.getSingle(event);
            if (type != null) {
                var idNumber = getSingleDefaultOrNull(event, idExpression, NMSApi.generateRandomEntityId());
                if (idNumber != null) {
                    int id = idNumber.intValue();
                    var bukkitEntityType = EntityUtils.toBukkitEntityType(type.data);
                    AddEntityPacket entitySpawnPacket = new AddEntityPacket(
                            id,
                            UUID.randomUUID(),
                            loc.getX(),
                            loc.getY(),
                            loc.getZ(),
                            loc.getPitch(),
                            loc.getYaw(),
                            bukkitEntityType,
                            0,
                            new Vector());
                    if (hasSection()) {
                        EntityMetadataPacket entityMetadataPacket = new EntityMetadataPacket(id, ExprSecEntityMetadata.getMetadata(event, bukkitEntityType.getEntityClass(), expressionMap));
                        NMSApi.sendBundledPackets(playersExpression.getAll(event), entitySpawnPacket, entityMetadataPacket);
                    } else {
                        NMSApi.sendPackets(playersExpression.getAll(event), entitySpawnPacket);
                    }
                }
            }
        }
        return super.walk(event, false);
    }

    @Override
    public boolean initNormal(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        return !(getParser().getNode() instanceof SectionNode);
    }

    @Override
    public boolean initSection(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = ExprSecEntityMetadata.VALIDATOR.validate(node);
        if (container == null) return false;
        expressionMap = ExprSecEntityMetadata.getMetadataExpressionMap(entityTypeExpression, container);
        return expressionMap != null;
    }
}
