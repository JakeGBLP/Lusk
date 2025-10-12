package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprTeleportId extends SimplePropertyExpression<PlayerPositionPacket, Integer> {

    static {
        register(ExprTeleportId.class, Integer.class, "teleport id", "playerpositionpackets");
    }

    @Override
    public @Nullable Integer convert(PlayerPositionPacket from) {
        return from.getTeleportId();
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[]{Integer.class};
            default -> null;
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        if (delta instanceof Integer[] integers && delta.length > 0) {
            int integer = integers[0];
            for (PlayerPositionPacket playerPositionPacket : getExpr().getAll(event)) {
                int teleportId = playerPositionPacket.getTeleportId();
                switch (mode) {
                    case ADD -> teleportId += integer;
                    case REMOVE -> teleportId  -= integer;
                }
                playerPositionPacket.setTeleportId(teleportId);
            }
        }
    }

    @Override
    protected String getPropertyName() {
        return "teleport id";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}