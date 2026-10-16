package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

public class ExprSecPlayerRotationPacket extends SectionExpression<PlayerRotationPacket> {

    public static final EntryValidator VALIDATOR = EntryValidator.builder()
            .addEntryData(new ExpressionEntryData<>("yaw", null, false, Float.class))
            .addEntryData(new ExpressionEntryData<>("pitch", null, false, Float.class))
            .build();
    private Expression<Number> yawExpression, pitchExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprSecPlayerRotationPacket.class, ExprSecPlayerRotationPacket::new, PlayerRotationPacket.class)
                .patterns("[a] new player rotation packet with")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        yawExpression = (Expression<Number>) container.getOptional("yaw", false);
        if (yawExpression == null) return false;
        pitchExpression = (Expression<Number>) container.getOptional("pitch", false);
        return pitchExpression != null;
    }


    @Override
    protected PlayerRotationPacket @Nullable [] get(Event event) {
        Number yaw = yawExpression.getSingle(event);
        if (yaw == null) return new PlayerRotationPacket[0];
        Number pitch = pitchExpression.getSingle(event);
        if (pitch == null) return new PlayerRotationPacket[0];
        return new PlayerRotationPacket[]{new PlayerRotationPacket(yaw.floatValue(), pitch.floatValue())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PlayerRotationPacket> getReturnType() {
        return PlayerRotationPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "new player rotation packet";
    }
}
