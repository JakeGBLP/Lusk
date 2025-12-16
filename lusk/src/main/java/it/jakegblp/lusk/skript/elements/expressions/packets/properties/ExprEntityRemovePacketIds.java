package it.jakegblp.lusk.skript.elements.expressions.packets.properties;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.RemoveEntitiesPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.skript.elements.types.Types.REMOVE_ENTITIES_PACKET_CHANGER;

public class ExprEntityRemovePacketIds extends PropertyExpression<RemoveEntitiesPacket, Integer> {

    static {
        register(ExprEntityRemovePacketIds.class, Integer.class, "entity ids", "entityremovepackets");
    }

    @Override
    protected Integer[] get(Event event, RemoveEntitiesPacket[] source) {
        return CommonUtils.flatMap(Integer.class, source, RemoveEntitiesPacket::getEntityIds);
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "bundle packets of "+getExpr().toString(event, debug);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return REMOVE_ENTITIES_PACKET_CHANGER.acceptChange(mode);
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        REMOVE_ENTITIES_PACKET_CHANGER.change(getExpr().getAll(event), delta, mode);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends RemoveEntitiesPacket>) expressions[0]);
        return false;
    }
}

