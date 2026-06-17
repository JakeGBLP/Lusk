package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

public class ExprAttributeSnapshot extends SimpleExpression<AttributeSnapshot> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprAttributeSnapshot.class, ExprAttributeSnapshot::new, AttributeSnapshot.class,
                "[[a] new|a[n]] %attributetype% attribute with base [value] %number% [and [with] %-attributemodifiers%]");
    }

    private Expression<Attribute> attributeExpression;
    private Expression<Number> baseExpression;
    private Expression<AttributeModifier> attributeModifierExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.attributeExpression = (Expression<Attribute>) expressions[0];
        this.baseExpression = (Expression<Number>) expressions[1];
        this.attributeModifierExpression = (Expression<AttributeModifier>) expressions[2];
        return true;
    }

    @Override
    protected AttributeSnapshot @Nullable [] get(Event event) {
        Attribute attribute = attributeExpression.getSingle(event);
        if (attribute == null) return new AttributeSnapshot[0];
        Number base = baseExpression.getSingle(event);
        if (base == null) return new AttributeSnapshot[0];
        var modifiers = attributeModifierExpression.getArray(event);
        return new AttributeSnapshot[] {
                modifiers == null ? new AttributeSnapshot(attribute, base.doubleValue()) : new AttributeSnapshot(attribute, base.doubleValue(), List.of(modifiers))
        };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AttributeSnapshot> getReturnType() {
        return AttributeSnapshot.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new " + attributeExpression.toString(event, debug) + " attribute with base value " + baseExpression.toString(event, debug);
    }
}
