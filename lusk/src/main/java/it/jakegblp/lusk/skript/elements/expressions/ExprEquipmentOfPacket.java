package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SetEquipmentPacket;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ExprEquipmentOfPacket extends PropertyExpression<SetEquipmentPacket, ItemType> {

    static {
        registerDefault(ExprEquipmentOfPacket.class, ItemType.class, "%equipmentslots% packet equipment", "equipmentpackets");
    }

    private Expression<EquipmentSlot> equipmentSlotExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            equipmentSlotExpression = (Expression<EquipmentSlot>) expressions[0];
            setExpr((Expression<SetEquipmentPacket>) expressions[1]);
        } else {
            setExpr((Expression<SetEquipmentPacket>) expressions[0]);
            equipmentSlotExpression = (Expression<EquipmentSlot>) expressions[1];
        }
        return true;
    }


    @Override
    protected ItemType[] get(Event event, SetEquipmentPacket[] source) {
        EquipmentSlot equipmentSlot = equipmentSlotExpression.getSingle(event);
        if (equipmentSlot == null) return null;
        return Arrays.stream(source).flatMap(setEquipmentPacket -> setEquipmentPacket.getEquipment().values().stream().map(ItemType::new)).toArray(ItemType[]::new);
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class<?>[]{ItemType.class};
            case DELETE -> new Class<?>[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        EquipmentSlot[] equipmentSlots = equipmentSlotExpression.getAll(event);
        if (equipmentSlots == null || equipmentSlots.length == 0) return;
        if (mode == Changer.ChangeMode.DELETE) {
            for (SetEquipmentPacket setEquipmentPacket : getExpr().getAll(event)) {
                setEquipmentPacket.remove(equipmentSlots);
            }
        } else if (delta != null && delta[0] instanceof ItemType itemType) {
            for (SetEquipmentPacket setEquipmentPacket : getExpr().getAll(event)) {
                for (EquipmentSlot slot : equipmentSlots) {
                    setEquipmentPacket.set(slot, itemType.getRandom());
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return equipmentSlotExpression.toString(event, debug) + " packet equipment of "+getExpr().toString(event, debug);
    }
}
