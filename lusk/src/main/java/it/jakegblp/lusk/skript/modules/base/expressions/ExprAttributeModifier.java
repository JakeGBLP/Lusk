package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprAttributeModifier extends SimpleExpression<AttributeModifier> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprAttributeModifier.class, ExprAttributeModifier::new, AttributeModifier.class,
                "[an] increase (by|of) %number% with key %namespacedkey% [in [the] %-equipmentslotgroup%]",
                "[a] base multiplier (by|of) %number% with key %namespacedkey% [in [the] %-equipmentslotgroup%]",
                "[a] total multiplier (by|of) %number% with key %namespacedkey% [in [the] %-equipmentslotgroup%]");
    }

    private AttributeModifier.Operation operation;
    private Expression<Number> baseExpression;
    private Expression<NamespacedKey> keyExpression;
    private Expression<EquipmentSlotGroup> equipmentSlotGroupExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        operation = AttributeModifier.Operation.values()[matchedPattern];
        baseExpression = (Expression<Number>) expressions[0];
        keyExpression = (Expression<NamespacedKey>) expressions[1];
        equipmentSlotGroupExpression = (Expression<EquipmentSlotGroup>) expressions[2];
        return true;
    }

    @Override
    protected AttributeModifier @Nullable [] get(Event event) {
        Number number = baseExpression.getSingle(event);
        if (number == null) return new AttributeModifier[0];
        NamespacedKey key = keyExpression.getSingle(event);
        if (key == null) return new AttributeModifier[0];
        EquipmentSlotGroup finalGroup = EquipmentSlotGroup.ANY;
        if (equipmentSlotGroupExpression != null) {
            var group = equipmentSlotGroupExpression.getSingle(event);
            if (group != null) finalGroup = group;
        }
        return new AttributeModifier[]{new AttributeModifier(key, number.doubleValue(), operation, finalGroup)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AttributeModifier> getReturnType() {
        return AttributeModifier.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return switch (operation) {
            case ADD_NUMBER -> "an increase";
            case ADD_SCALAR -> "a base multiplier";
            case MULTIPLY_SCALAR_1 -> "a total multiplier";
        } + " by " + baseExpression.toString(event, debug) + " with key " + keyExpression.toString(event, debug);
    }
}
