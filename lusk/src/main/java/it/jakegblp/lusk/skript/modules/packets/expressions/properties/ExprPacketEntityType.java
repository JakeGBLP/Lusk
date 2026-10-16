package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.entity.EntityData;
import it.jakegblp.lusk.nms.core.protocol.packets.client.AddEntityPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPacketEntityType extends SimplerPropertyExpression<AddEntityPacket, EntityData> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPacketEntityType.class, ExprPacketEntityType::new, EntityData.class)
                .propertyOf("packet entity type", "entityspawnpackets")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public @Nullable EntityData convert(AddEntityPacket from) {
        return EntityUtils.toSkriptEntityData(from.getEntityType());
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{EntityData.class} : null;
    }

    @Override
    public void set(AddEntityPacket from, EntityData to) {
        from.setEntityType(EntityUtils.toBukkitEntityType(to));
    }

    @Override
    protected String getPropertyName() {
        return "packet entity type";
    }

    @Override
    public Class<? extends EntityData> getReturnType() {
        return EntityData.class;
    }
}