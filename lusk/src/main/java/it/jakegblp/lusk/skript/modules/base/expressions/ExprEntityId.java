package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithEntityId;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprEntityId extends SimplePropertyExpression<Object, Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprEntityId.class, ExprEntityId::new, Integer.class)
                .propertyOf("entity [protocol] id", "entities" + (AbstractNMS.NMS == null ? "" : "/packets"))
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public @Nullable Integer convert(Object from) {
        return switch (from) {
            case Entity entity -> entity.getEntityId();
            case PacketWithEntityId packet -> packet.getEntityId();
            case null, default -> null;
        };
    }

    @Override
    protected String getPropertyName() {
        return "entity id";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}