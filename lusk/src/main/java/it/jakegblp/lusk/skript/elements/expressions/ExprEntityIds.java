package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.protocol.packets.client.RemoveEntitiesPacket;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static it.jakegblp.lusk.skript.elements.types.Types.REMOVE_ENTITIES_PACKET_CHANGER;

public class ExprEntityIds extends PropertyExpression<RemoveEntitiesPacket, Integer> {

    static {
        register(ExprEntityIds.class, Integer.class, "entity ids", "entityremovepackets");
    }

    @Override
    protected Integer[] get(Event event, RemoveEntitiesPacket[] source) {
        return Arrays.stream(source).flatMapToInt(removeEntitiesPacket -> removeEntitiesPacket.getEntityIds().intStream()).boxed().toArray(Integer[]::new);
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

