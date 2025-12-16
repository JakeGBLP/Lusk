package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;

public class ExprSecPlayerRotationPacket extends SectionExpression<PlayerRotationPacket> {

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("yaw", null, false, Float.class))
                .addEntryData(new ExpressionEntryData<>("pitch", null, false, Float.class))
                .build();
        Skript.registerExpression(ExprSecPlayerRotationPacket.class, PlayerRotationPacket.class, ExpressionType.COMBINED,
                "[a] new player rotation packet with yaw %number%[,] and [with] pitch %number%");
    }

    private Expression<Number> yawExpression, pitchExpression;

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
        return new PlayerRotationPacket[] { new PlayerRotationPacket(yaw.floatValue(), pitch.floatValue()) };
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
