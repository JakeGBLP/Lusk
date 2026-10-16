package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SetEquipmentPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPacketEquipment extends PropertyExpression<SetEquipmentPacket, ItemType> {

    private Expression<EquipmentSlot> equipmentSlotExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPacketEquipment.class, ExprPacketEquipment::new, ItemType.class)
                .propertyOf("%equipmentslots% packet equipment", "equipmentpackets")
                .register(syntaxRegistry);
    }

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
        return CommonUtils.flatMap(ItemType.class, source, setEquipmentPacket -> setEquipmentPacket.getEquipment().values(), ItemType::new);
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
        if (mode == Changer.ChangeMode.DELETE)
            for (SetEquipmentPacket setEquipmentPacket : getExpr().getAll(event))
                setEquipmentPacket.remove(equipmentSlots);
        else if (delta != null && delta[0] instanceof ItemType itemType)
            for (SetEquipmentPacket setEquipmentPacket : getExpr().getAll(event))
                for (EquipmentSlot slot : equipmentSlots)
                    setEquipmentPacket.set(slot, itemType.getRandom());
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return equipmentSlotExpression.toString(event, debug) + " packet equipment of " + getExpr().toString(event, debug);
    }
}
