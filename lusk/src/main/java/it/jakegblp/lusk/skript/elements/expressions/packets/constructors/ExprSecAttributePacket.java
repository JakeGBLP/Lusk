package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.UpdateAttributesPacket;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.UUID;

// poa stuff

public class ExprSecAttributePacket extends SectionExpression<UpdateAttributesPacket> {

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("attribute", null, false, Attribute.class))
                .addEntryData(new ExpressionEntryData<>("value", null, false, Number.class))
                .addEntryData(new ExpressionEntryData<>("id", null, false, Number.class))
                .build();

        Skript.registerExpression(ExprSecAttributePacket.class, UpdateAttributesPacket.class, ExpressionType.COMBINED,
                "[a] new attribute packet"
        );
    }

    private Expression<Attribute> attributeExpression;
    private Expression<Number> valueExpression;
    private Expression<Number> idExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed,
                        SkriptParser.ParseResult result, @Nullable SectionNode node,
                        @Nullable List<TriggerItem> triggerItems) {

        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;

        attributeExpression = (Expression<Attribute>) container.getOptional("attribute", false);
        valueExpression = (Expression<Number>) container.getOptional("value", false);
        idExpression = (Expression<Number>) container.getOptional("id", false);

        return attributeExpression != null && valueExpression != null && idExpression != null;
    }

    @Override
    protected UpdateAttributesPacket @Nullable [] get(Event event) {
        Attribute attribute = attributeExpression.getSingle(event);
        Number value = valueExpression.getSingle(event);
        Number id = idExpression.getSingle(event);

        if (attribute == null || value == null || id == null) return new UpdateAttributesPacket[0];

        final double base = value.doubleValue();
        // todo: figure this out and redo it
        return new UpdateAttributesPacket[]{new UpdateAttributesPacket(id.intValue(), List.of(new AttributeSnapshot(attribute, base, List.of(new AttributeModifier(UUID.randomUUID(), "poa", base, AttributeModifier.Operation.ADD_NUMBER)))))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends UpdateAttributesPacket> getReturnType() {
        return UpdateAttributesPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "new attribute packet";
    }
}
