package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacketWithEntityId;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprEntityId extends SimplePropertyExpression<Object, Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerPropertyExpression(syntaxRegistry, ExprEntityId.class, ExprEntityId::new, Integer.class, "entity [protocol] id", "entities/packets", true);
    }

    @Override
    public @Nullable Integer convert(Object from) {
        if (from instanceof Entity entity)
            return entity.getEntityId();
        else if (from instanceof ClientboundPacketWithEntityId packet)
            return packet.getEntityId();
        return null;
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