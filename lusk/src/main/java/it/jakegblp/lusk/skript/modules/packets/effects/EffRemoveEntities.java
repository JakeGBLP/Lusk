package it.jakegblp.lusk.skript.modules.packets.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.RemoveEntitiesPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffRemoveEntities extends Effect {

    private Expression<Object> entityExpression;
    private Expression<Player> playerExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffRemoveEntities.class, EffRemoveEntities::new)
                .patterns("remove entit(y|ies) %entities/integers% for %players%")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        var players = playerExpression.getAll(event);
        var packet = new RemoveEntitiesPacket();
        for (Object object : entityExpression.getAll(event)) {
            if (object instanceof Entity entity) {
                packet.add(entity.getEntityId());
            } else if (object instanceof Number number) {
                packet.add(number.intValue());
            }
        }
        NMSApi.sendPacket(players, packet, ExecutionMode.SYNCHRONOUS);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "remove " + entityExpression.toString(event, debug) + " for " + playerExpression.toString(event, debug);
    }
}
