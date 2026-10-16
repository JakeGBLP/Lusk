package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

@Name("Armor Stand - Disabled Slots")
@Description("All the disabled slots of an armor stand.\nCan be set, removed from, added to, reset and deleted.")
@Example("add chest slot to disabled slots of {_armorStand}")
@Since("1.3")
public class ExprArmorStandDisabledSlots extends PropertyExpression<LivingEntity, EquipmentSlot> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprArmorStandDisabledSlots.class, ExprArmorStandDisabledSlots::new, EquipmentSlot.class)
                .propertyOf("disabled slots", "livingentities")
                .register(syntaxRegistry);
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
        var armorStands = CommonUtils.filter(ArmorStand.class, getExpr().getArray(event));
        switch (mode) {
            case SET, ADD, REMOVE -> {
                if (delta != null && delta.length > 0) {
                    var slots = CommonUtils.filter(EquipmentSlot.class, delta);
                    for (ArmorStand armorStand : armorStands) {
                        switch (mode) {
                            case SET -> armorStand.setDisabledSlots(slots);
                            case ADD -> armorStand.addDisabledSlots(slots);
                            case REMOVE -> armorStand.removeDisabledSlots(slots);
                        }
                    }
                }
            }
            case RESET, DELETE -> {
                for (ArmorStand armorStand : armorStands)
                    armorStand.setDisabledSlots();
            }
        }
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[]{(EquipmentSlot[].class)};
            case DELETE, RESET -> new Class[0];
            default -> null;
        };
    }
}