package it.jakegblp.lusk.skript.elements.expressions.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import it.jakegblp.lusk.skript.api.OptionallySectionExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;

public class ExprSecPlayerRotationPacket extends OptionallySectionExpression<PlayerRotationPacket> {

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("yaw", null, false, Float.class))
                .addEntryData(new ExpressionEntryData<>("pitch", null, false, Float.class))
                .build();
        Skript.registerExpression(ExprSecPlayerRotationPacket.class, PlayerRotationPacket.class, ExpressionType.COMBINED,
                "new player rotation packet", "new player rotation packet with yaw %number%[,] and [with] pitch %number%");
    }

    private Expression<Number> yawExpression, pitchExpression;

    @Override
    public boolean hasSection(int pattern, SkriptParser.ParseResult result) {
        return pattern != 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean initNormal(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        yawExpression = (Expression<Number>) expressions[0];
        pitchExpression = (Expression<Number>) expressions[1];
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean initSection(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
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
        return "new player rotation packet" + (hasSection() ? "" : " with yaw "+yawExpression.toString(event, debug) + " and with pitch "+pitchExpression.toString());
    }
}
