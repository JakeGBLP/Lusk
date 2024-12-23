package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Stream;

import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;

@Name("Armor Stand - Disabled Slots")
@Description("All the disabled slots of an armor stand.\nCan be set, remove from, added to, reset and deleted.")
@Examples("add chest slot to disabled slots of {_armorStand}")
@Since("1.3")
public class ExprArmorStandDisabledSlots extends PropertyExpression<LivingEntity, EquipmentSlot> {

    static {
        register(ExprArmorStandDisabledSlots.class, EquipmentSlot.class, ARMOR_STAND_PREFIX + " disabled [equipment] slots", "livingentities");
    }

    @Override
    protected EquipmentSlot[] get(Event event, LivingEntity[] source) {
        return Arrays.stream(source)
                .filter(ArmorStand.class::isInstance)
                .map(ArmorStand.class::cast)
                .flatMap(armorStand -> armorStand.getDisabledSlots().stream())
                .toArray(EquipmentSlot[]::new);
    }

    @Override
    public Class<? extends EquipmentSlot> getReturnType() {
        return EquipmentSlot.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "armor stand disabled equipment slots of " + getExpr().toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<LivingEntity>) expressions[0]);
        return true;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        Stream<ArmorStand> armorStands = getExpr().stream(event)
                .filter(ArmorStand.class::isInstance)
                .map(ArmorStand.class::cast);
        switch (mode) {
            case SET, ADD, REMOVE -> {
                if (delta != null && delta.length > 0) {
                    EquipmentSlot[] slots = (EquipmentSlot[]) delta;
                    armorStands.forEach(armorStand -> {
                        switch (mode) {
                            case SET -> armorStand.setDisabledSlots(slots);
                            case ADD -> armorStand.addDisabledSlots(slots);
                            case REMOVE -> armorStand.removeDisabledSlots(slots);
                        }
                    });
                }
            }
            case RESET, DELETE -> armorStands.forEach(ArmorStand::setDisabledSlots);
        }
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET,ADD,REMOVE -> CollectionUtils.array(EquipmentSlot[].class);
            case DELETE, RESET -> CollectionUtils.array();
            default -> null;
        };
    }
}
