package it.jakegblp.lusk.elements.packets.entities.entity.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static it.jakegblp.lusk.utils.NMSUtils.NMS;
import static it.jakegblp.lusk.utils.SkriptUtils.getSingle;

@Name("Packets | Entity - Spawn Fake Entity")
@Description("""
Spawns a fake entity for the provided players.
Parameters:
- EntityType: the type of the entity to spawn
- Id: protocol id of the entity to spawn, random if not set
- UUID: the UUID of the entity to spawn, random if not set
- Velocity: the velocity to spawn the entity with, ignored if not set
- Head Pitch:
""")
@Examples("send entity wake up animation packet for target to player")
@Since("1.4")
public class EffPacketEntitySpawn extends Effect {

    static {
        if (NMS != null)
            Skript.registerEffect(EffPacketEntitySpawn.class,
                    "spawn fake %entitytype% [with id %-number%[,]] [(and|[and] with) uuid %-string%[,]] [(and|[and] with) velocity %-vector%[,]] [(and|[and] with) head pitch %-number%[,]] [(and|[and] with) data %-number%] %directions% %location% (for|to) %players%"
        );
    }

    private Expression<EntityType> entityTypeExpression;
    private Expression<Number> idExpression;
    private Expression<String> uuidExpression;
    private Expression<Vector> vectorExpression;
    private Expression<Number> headPitchExpression;
    private Expression<Number> dataExpression;
    private Expression<Location> locationExpression;
    private Expression<Player> playerExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityTypeExpression = (Expression<EntityType>) expressions[0];
        idExpression = (Expression<Number>) expressions[1];
        uuidExpression = (Expression<String>) expressions[2];
        vectorExpression = (Expression<Vector>) expressions[3];
        headPitchExpression = (Expression<Number>) expressions[4];
        dataExpression = (Expression<Number>) expressions[5];
        locationExpression = Direction.combine((Expression<Direction>) expressions[6], (Expression<Location>) expressions[7]);
        playerExpression = (Expression<Player>) expressions[8];
        return true;
    }

    @Override
    protected void execute(Event event) {
        assert NMS != null;
        EntityType entityType = entityTypeExpression.getSingle(event);
        if (entityType == null) return;
        Location location = locationExpression.getSingle(event);
        if (location == null) return;
        Vector velocity = getSingle(vectorExpression, event);
        if (velocity == null) velocity = new Vector();
        String uuidString = getSingle(uuidExpression, event);
        UUID uuid = null;
        if (uuidString != null) {
            try {
                uuid = UUID.fromString(uuidString);
            } catch (IllegalArgumentException ignored) {}
        }
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        Number headPitch = getSingle(headPitchExpression, event);
        if (headPitch == null) headPitch = 0;
        Number data = getSingle(dataExpression,event);
        if (data == null) data = 0;
        Number numberId = getSingle(idExpression, event);
        Integer id = numberId == null ? null : numberId.intValue();
        NMS.sendEntitySpawnPacket(
                id,
                uuid,
                EntityUtils.toBukkitEntityType(entityType.data),
                location.toVector(),
                location.getYaw(),
                location.getPitch(),
                headPitch.doubleValue(),
                data.intValue(),
                velocity,
                playerExpression.getAll(event));

    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        StringBuilder builder = new StringBuilder("spawn fake ").append(entityTypeExpression.toString(event, debug));
        if (idExpression != null) builder.append(" with id ").append(idExpression.toString(event, debug));
        if (uuidExpression != null) builder.append(" with uuid ").append(uuidExpression.toString(event, debug));
        if (vectorExpression != null) builder.append(" with velocity ").append(vectorExpression.toString(event, debug));
        if (headPitchExpression != null) builder.append(" with head yaw ").append(headPitchExpression.toString(event, debug));
        if (dataExpression != null) builder.append(" with data ").append(dataExpression.toString(event, debug));
        return builder.append(" ").append(locationExpression.toString(event, debug)).append(" to ")
                .append(playerExpression.toString(event, debug)).toString();
    }

}
