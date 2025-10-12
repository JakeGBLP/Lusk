package it.jakegblp.lusk.skript.elements.expressions.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.RemoveEntitiesPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ExprRemoveEntitiesPacket extends SimpleExpression<RemoveEntitiesPacket> {

    static {
        Skript.registerExpression(ExprRemoveEntitiesPacket.class, RemoveEntitiesPacket.class, ExpressionType.SIMPLE,
                "[an|[a] new] remove entit(ies|y) packet (with|using) [id[s]] %integers%",
                "[an|[a] new] entit(ies|y) remove packet (with|using) [id[s]] %integers%");
    }

    private Expression<Integer> idsExpression;

    @Override
    protected RemoveEntitiesPacket @Nullable [] get(Event event) {
        return new RemoveEntitiesPacket[] {new RemoveEntitiesPacket(Arrays.asList(idsExpression.getAll(event)))};
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        idsExpression = (Expression<Integer>) expressions[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends RemoveEntitiesPacket> getReturnType() {
        return RemoveEntitiesPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new remove entities packet with " + idsExpression.toString(event, debug);
    }
}
