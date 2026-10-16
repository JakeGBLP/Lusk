package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoRemovePacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.UUID;

public class ExprPacketPlayerInfoUUIDs extends PropertyExpression<PlayerInfoRemovePacket, UUID> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPacketPlayerInfoUUIDs.class, ExprPacketPlayerInfoUUIDs::new, UUID.class)
                .propertyOf("[packet] uuids", "playerinforemovepackets")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    protected UUID[] get(Event event, PlayerInfoRemovePacket[] source) {
        return CommonUtils.flatMap(UUID.class, source, PlayerInfoRemovePacket::getPlayers);
    }

    @Override
    public Class<? extends UUID> getReturnType() {
        return UUID.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "packet uuids of " + getExpr().toString(event, debug);
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, SET, REMOVE -> new Class[] {UUID[].class};
            default -> null;
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        if (delta != null && delta.length > 0) {
            UUID[] uuids = CommonUtils.filter(UUID.class, delta);
            if (uuids.length == 0) return;
            for (var packet : getExpr().getArray(event)) {
                switch (mode) {
                    case SET -> packet.set(uuids);
                    case ADD -> packet.add(uuids);
                    case REMOVE -> packet.remove(uuids);
                }
            }
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends PlayerInfoRemovePacket>) expressions[0]);
        return true;
    }
}

