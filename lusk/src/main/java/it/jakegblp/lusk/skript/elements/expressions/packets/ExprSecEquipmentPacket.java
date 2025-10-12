package it.jakegblp.lusk.skript.elements.expressions.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SetEquipmentPacket;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExprSecEquipmentPacket extends SectionExpression<SetEquipmentPacket> {

    public static final EntryValidator VALIDATOR;

    static {
        var builder = EntryValidator.builder().addEntryData(new ExpressionEntryData<>("id", null, false, Integer.class));
        for (EquipmentSlot value : EquipmentSlot.values())
            builder.addEntryData(
                    new ExpressionEntryData<>(value.name().replace('_', ' ').toLowerCase(Locale.ROOT), null, true, ItemType.class));
        VALIDATOR = builder.build();

        Skript.registerExpression(ExprSecEquipmentPacket.class, SetEquipmentPacket.class, ExpressionType.COMBINED,
                "[a] new ([set] equipment|equipment set) packet");
    }

    private Expression<Integer> idExpression;
    private Map<EquipmentSlot, Expression<ItemType>> expressionMap;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        idExpression = (Expression<Integer>) container.getOptional("id", false);
        if (idExpression == null) return false;
        for (EquipmentSlot value : EquipmentSlot.values()) {
            Expression<ItemType> expression = (Expression<ItemType>) container.getOptional(
                    value.name().replace('_', ' ').toLowerCase(Locale.ROOT), false);
            if (expression != null) {
                if (expressionMap == null) expressionMap = new HashMap<>();
                expressionMap.put(value, expression);
            }
        }
        if (expressionMap == null || expressions.length >= 1) {
            Skript.error("At least one slot must be provided!");
            return false;
        }
        return true;
    }

    @Override
    protected SetEquipmentPacket @Nullable [] get(Event event) {
        Integer id = idExpression.getSingle(event);
        if (id == null) return new SetEquipmentPacket[0];
        var map = new HashMap<EquipmentSlot, ItemStack>();
        expressionMap.forEach((key, expression) -> {
            ItemType itemType = expression.getSingle(event);
            if (itemType != null && !itemType.isEmpty() && !itemType.isAll()) {
                map.put(key, itemType.getRandom());
            }
        });
        return new SetEquipmentPacket[] {new SetEquipmentPacket(id, map)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SetEquipmentPacket> getReturnType() {
        return SetEquipmentPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new equipment set packet";
    }
}
