package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.AttributePacket;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;

@SuppressWarnings("ALL")
public class ExprSecAttributePacket extends SectionExpression<AttributePacket> {

    public static final EntryValidator VALIDATOR;

    static {
        registerAttributeType();

        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("attribute", null, false, Attribute.class))
                .addEntryData(new ExpressionEntryData<>("value", null, false, Number.class))
                .addEntryData(new ExpressionEntryData<>("id", null, false, Number.class))
                .build();

        Skript.registerExpression(
                ExprSecAttributePacket.class,
                AttributePacket.class,
                ExpressionType.SIMPLE,
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
    protected AttributePacket @Nullable [] get(Event event) {
        Attribute attribute = attributeExpression.getSingle(event);
        Number value = valueExpression.getSingle(event);
        Number id = idExpression.getSingle(event);

        if (attribute == null || value == null || id == null) return new AttributePacket[0];

        return new AttributePacket[]{new AttributePacket(id.intValue(), attribute, value.doubleValue())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AttributePacket> getReturnType() {
        return AttributePacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "new attribute packet";
    }

    private static void registerAttributeType() {
        if (Classes.getExactClassInfo(Attribute.class) != null) return;

        Classes.registerClass(new ClassInfo<>(Attribute.class, "attribute")
                .user("attributes?")
                .name("Attribute")
                .description("Bukkit attribute (e.g. scale)")
                .parser(new Parser<>() {
                    @Override
                    public @Nullable Attribute parse(@NotNull String input, @NotNull ParseContext context) {
                        return parseAttribute(input);
                    }

                    @Override
                    public @NotNull String toString(Attribute a, int flags) {
                        return a.name().toLowerCase();
                    }

                    @Override
                    public @NotNull String toVariableNameString(Attribute a) {
                        return a.name().toLowerCase();
                    }
                })
        );
    }

    private static Attribute parseAttribute(String input) {
        String s = input.trim()
                .toUpperCase()
                .replace(' ', '_')
                .replace('-', '_');

        try {
            return Attribute.valueOf(s);
        } catch (IllegalArgumentException ignored) {
        }

        for (Attribute a : Attribute.values()) {
            if (a.name().endsWith("_" + s)) return a;
        }

        return null;
    }
}
