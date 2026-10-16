package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.UpdateAttributesPacket;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPacketAttributes extends PropertyExpression<UpdateAttributesPacket, AttributeSnapshot> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPacketAttributes.class, ExprPacketAttributes::new, AttributeSnapshot.class)
                .propertyOf("packet attributes", "updateattributespacket")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    protected AttributeSnapshot[] get(Event event, UpdateAttributesPacket[] source) {
        return CommonUtils.flatMap(AttributeSnapshot.class, source, UpdateAttributesPacket::getAttributeSnapshots);
    }

    @Override
    public Class<? extends AttributeSnapshot> getReturnType() {
        return AttributeSnapshot.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "packet attributes of " + getExpr().toString(event, debug);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends UpdateAttributesPacket>) expressions[0]);
        return true;
    }
}

