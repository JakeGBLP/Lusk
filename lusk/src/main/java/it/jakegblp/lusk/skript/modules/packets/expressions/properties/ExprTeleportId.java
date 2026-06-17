package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprTeleportId extends SimplePropertyExpression<PlayerPositionPacket, Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerPropertyExpression(syntaxRegistry, ExprTeleportId.class, ExprTeleportId::new, Integer.class,
                "[packet] teleport id", "playerpositionpackets");
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
        if (delta != null && delta.length > 0 && delta[0] instanceof Integer integer) {
            for (PlayerPositionPacket playerPositionPacket : getExpr().getAll(event)) {
                int teleportId = playerPositionPacket.getTeleportId();
                switch (mode) {
                    case ADD -> teleportId += integer;
                    case REMOVE -> teleportId -= integer;
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